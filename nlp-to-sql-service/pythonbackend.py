from flask import Flask, request, jsonify, session, Blueprint
from flask_cors import CORS
from flask_session import Session
from dotenv import load_dotenv
from pydantic import BaseModel, Field
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnablePassthrough
from langchain_core.output_parsers import JsonOutputParser
from langchain_community.utilities import SQLDatabase
from langchain_core.output_parsers import StrOutputParser
from langchain_google_genai import ChatGoogleGenerativeAI
import requests
import ast
import json
from datetime import datetime
import psycopg2
import py_eureka_client.eureka_client as eureka_client



app = Flask(__name__)
CORS(app)


# Configure Flask-Session
app.config['SESSION_TYPE'] = 'filesystem'
Session(app)

eureka_client.init(eureka_server="http://localhost:8761/eureka/",
                   app_name="nlp-to-sql-service",
                   instance_host="localhost",
                   instance_port=5000)

load_dotenv()

chathistorydburi = 'mysql+mysqlconnector://root:140203@localhost:3306/nltosqlservice'
chathistorydb = SQLDatabase.from_uri(chathistorydburi)



class TraLoi(BaseModel):
    sql_query: str = Field(description="SQL query")
    natural_language: str = Field(description="Natural language response")
    kind_of_chart: str = Field(description="kind of chart")
    data: str = Field(description="data to make chart base on response include labels and values")
parser = JsonOutputParser(pydantic_object=TraLoi)


def get_chat_history():
    return chathistorydb.run("SELECT sender, content FROM chat_history")


def init_database(type: str, dbms: str,user: str, password: str, host: str, port: str, database: str) -> SQLDatabase:
    db_uri = None
    if type == 'sql':
        if dbms == 'mysql':
            db_uri = f"mysql+mysqlconnector://{user}:{password}@{host}:{port}/{database}"
        if dbms == 'postgresql':
            db_uri = f"postgresql+psycopg2://{user}:{password}@{host}:{port}/{database}"
    return SQLDatabase.from_uri(db_uri)


def get_sql_chain(db):
    template = """
    You are a data analyst at a company. You are interacting with a user who is asking you questions about the company's database.
    Based on the table schema below, write a SQL query that would answer the user's question. Take the conversation history into account.
    
    <SCHEMA>{schema}</SCHEMA>
    
    Conversation History: {chat_history}
    
    Write only the SQL query and nothing else. Do not wrap the SQL query in any other text, not even backticks.
    
    For example:
    Question: which 3 artists have the most tracks?
    SQL Query: SELECT ArtistId, COUNT(*) as track_count FROM Track GROUP BY ArtistId ORDER BY track_count DESC LIMIT 3;
    Question: Name 10 artists
    SQL Query: SELECT Name FROM Artist LIMIT 10;
    
    Your turn:
    
    Question: {question}
    SQL Query:
    """
    
    prompt = ChatPromptTemplate.from_template(template)
    
    llm = ChatGoogleGenerativeAI(model="gemini-1.5-pro")
    
    def get_schema(_):
        return db.get_table_info()
    
    return (
        RunnablePassthrough.assign(schema=get_schema)
        | prompt
        | llm
        | StrOutputParser()
    )

def get_response(user_query: str, db: SQLDatabase, chat_history: list):
    sql_chain = get_sql_chain(db)
    
    template = """
    You are a data analyst at a company. You are interacting with a user who is asking you questions about the company's database.
    Based on the table schema below, question, sql query, and sql response, write AI Message content in json format include sql query, a natural language response and From the results after querying, can we draw a chart from those results? If so, what kind of chart and data to make chart base on {response}? if not is None.
    If natural language response has single quote, double quote, or backslash, please delete it.
    Example:
    Question: What are the top 10 albums by total sales?
    AI Result in json format content:
              sql_query: SELECT al.Title, SUM(il.UnitPrice * il.Quantity) AS TotalSales
                          FROM album AS al
                          JOIN track AS t ON al.AlbumId = t.AlbumId
                          JOIN invoiceline AS il ON t.TrackId = il.TrackId
                          GROUP BY al.Title
                          ORDER BY TotalSales DESC
                          LIMIT 10;
              
              natural_language: Here are the top 10 albums by total sales:
                          Battlestar Galactica (Classic), Season 1: $35.82
                          Minha Historia: $34.65
                          The Office, Season 3: $31.84
                          Heroes, Season 1: $25.87
                          Lost, Season 2: $25.87
                          Greatest Hits: $25.74

              kind_of_chart: bar chart
              data: labels and values
    
                  
    <SCHEMA>{schema}</SCHEMA>

    Conversation History: {chat_history}
    SQL Query: <SQL>{query}</SQL>
    User question: {question}
    SQL Response: {response}"""
    
    prompt = ChatPromptTemplate.from_template(template)
    
    llm = ChatGoogleGenerativeAI(model="gemini-1.5-pro")
    
    chain = (
        RunnablePassthrough.assign(query=sql_chain).assign(
            schema=lambda _: db.get_table_info(),
            response=lambda vars: db.run(vars["query"]),
        )
        | prompt
        | llm
        | parser
    )
    
    return chain.invoke({
        "question": user_query,
        "chat_history": chat_history,
    })


#check connection to database
@app.route('/nlptosqlservice/check_connection', methods=['POST'])
def check_connection():
    data = request.json
    try:
        db = init_database(
            data['type'],
            data['dbms'],
            data['username'],
            data['password'],
            data['host'],
            data['port'],
            data['databaseName']
        )
        # Test the connection
        return jsonify({"message": "Connection successful!"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


#store from room service data to database
@app.route('/nlptosqlservice/data', methods=['POST'])
def get_datacreate_fromapi():
    try:
        data = request.get_json()
        required_fields = ["roomId","userId", "type", "dbms", "host", "port", "username", "databaseName", "status", "pythonReqChatHistoryDtos"]
        for field in required_fields:
            if field not in data:
                return jsonify({"error": f"Missing required field: {field}"}), 400
        chat_history = data.get("pythonReqChatHistoryDtos", [])
        for i in chat_history:
            chathistorydb.run(f"INSERT INTO chat_history (sender, content, time, status_insert, room_id) VALUES ('{i['sender']}', '{i['content']}','{i['time']}','old', '{data['roomId']}')")
        chathistorydb.run(f"INSERT INTO room (roomId ,userId, type, dbms, host, port, username, databaseName, password, status) VALUES ('{data['roomId']}','{data['userId']}', '{data['type']}', '{data['dbms']}', '{data['host']}', '{data['port']}', '{data['username']}', '{data['databaseName']}','{data['password']}','{data['status']}')")
        return jsonify(data), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


def custom_parser(string):
    # Replace placeholder with actual datetime construction logic
    data = eval(string, {"datetime": datetime})  # Use eval safely with restricted globals
    return data



#show room information and chat history from database
@app.route('/nlptosqlservice/connect_chat/<int:room_id>', methods=['GET'])
def connect_chat(room_id):
    data = chathistorydb.run(f"SELECT * FROM room WHERE roomId = {room_id}")
    string_data_chat = chathistorydb.run(f"SELECT * FROM chat_history WHERE room_id = {room_id}")
    string_data_chat = string_data_chat.replace("datetime.datetime", "")
    data_chat = custom_parser(string_data_chat)
    data_dict = ast.literal_eval(data)
    if not data:
        return jsonify({"error": "Room not found"}), 404
    
    try:
        session[room_id] = {
            "type": data_dict[0][2],
            "dbms": data_dict[0][3],
            "user": data_dict[0][6],
            "password": data_dict[0][8],
            "host": data_dict[0][4],
            "port": data_dict[0][5],
            "database": data_dict[0][7],
            "status": data_dict[0][9]
        }
        jsondata = {
            "type": data_dict[0][2],
            "dbms": data_dict[0][3],
            "user": data_dict[0][6],
            "password": data_dict[0][8],
            "host": data_dict[0][4],
            "port": data_dict[0][5],
            "database": data_dict[0][7],
            "status": data_dict[0][9],
            "ChatHistory": [
                {
                    "sender": item[1],
                    "content": item[2],
                    "time": datetime(*item[3]).isoformat(),  # Build datetime object and format it
                }
                for item in data_chat
            ]
        }
        jsonstring = json.dumps(jsondata, indent=4)
        json_data = json.loads(jsonstring)
        print(json_data)
        return jsonify(json_data), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    

#connect to database (case select disconnected room)
@app.route('/nlptosqlservice/connectchatroom', methods=['POST'])
def connect_chatroom():
    try:
        data = request.json
        room_id = data['room_id']
        session[room_id]['password'] = data['password']
        session.modified = True 
        print(session.get(room_id))
        db_params = session[room_id]
        init_database(
            db_params['type'],
            db_params['dbms'],
            db_params['user'],
            db_params['password'],
            db_params['host'],
            db_params['port'],
            db_params['database']
        )
        session[room_id]['status'] = 'connected'
        session.modified = True 
        return jsonify({"message": "Connected to database!"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    
#chat with AI
@app.route('/nlptosqlservice/query', methods=['POST'])
def query():
    data = request.json
    room_id = data['room_id']
    user_query = data['query']
    
    if room_id not in session:
        return jsonify({"error": "Database not connected"}), 500
    
    db_params = session[room_id]
    db = init_database(
        db_params['type'],
        db_params['dbms'],
        db_params['user'],
        db_params['password'],
        db_params['host'],
        db_params['port'],
        db_params['database']
    )
    chat_history = get_chat_history()
    
    chathistorydb.run(f"INSERT INTO chat_history (sender, content, status_insert, room_id) VALUES ('user', '{user_query}','new', '{room_id}')")
    
    response = get_response(user_query, db, chat_history)
    
    chathistorydb.run(f"INSERT INTO chat_history (sender, content, status_insert, room_id) VALUES ('AI', '{response['natural_language']}','new',{room_id})")
    
    return jsonify(response), 200

#get chat new from database
def get_chat_history_new(room_id):
    data_string = chathistorydb.run(f"SELECT sender, content, time FROM chat_history WHERE status_insert='new' AND room_id = {room_id}")
    return data_string

#save to room service database and close chat
@app.route('/nlptosqlservice/save_close', methods=['POST'])
def save_close():
    url = "http://localhost:8080/chatroom/save-chat-history"
    room_id = request.json['roomId']
    data_string = get_chat_history_new(room_id)
    data_string = data_string.replace("datetime.datetime", "")
    data_chat = custom_parser(data_string)

    headers = {
        'Content-Type': 'application/json'
    }
    jsondata = {
        "roomId": room_id,
        "chatHistoryDtos": [
            {
                "sender": item[0],
                "content": item[1],
                "time": datetime(*item[2]).isoformat(),  # Build datetime object and format it
            }
            for item in data_chat
        ]
    }
    jsonstring = json.dumps(jsondata, indent=4)
    json_data = json.loads(jsonstring)
    print(json_data)
    response = requests.post(url, json=json_data, headers=headers)
    requests.put(f"http://localhost:8080/chatroom/set-room-status/{room_id}/{'disconnected'}")
    chathistorydb.run(f"DELETE FROM chat_history WHERE room_id = {room_id}")
    chathistorydb.run(f"DELETE FROM room WHERE roomId = {room_id}")
    if response.status_code == 201:
        session.clear()
        return jsonify({"message": "Chat history saved!"}), 200
    else:
        return jsonify({"error": "Failed to save chat history"}), 500


if __name__ == '__main__':
    app.run(debug=True, port=5000)