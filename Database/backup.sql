--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: certificates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.certificates (
    id integer NOT NULL,
    user_id character varying,
    name character varying NOT NULL,
    issuer character varying,
    issue_date date,
    credential_id character varying,
    category character varying,
    url text,
    file_url text
);


ALTER TABLE public.certificates OWNER TO postgres;

--
-- Name: certificates_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.certificates_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.certificates_id_seq OWNER TO postgres;

--
-- Name: certificates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.certificates_id_seq OWNED BY public.certificates.id;


--
-- Name: course_nodes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course_nodes (
    id character varying(20) NOT NULL,
    name character varying(255) NOT NULL,
    link text,
    status character varying(20) DEFAULT 'unfinished'::character varying,
    avg_time_to_finish integer,
    roadmap_id character varying(20) NOT NULL,
    child_ids text
);


ALTER TABLE public.course_nodes OWNER TO postgres;

--
-- Name: roadmaps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roadmaps (
    id character varying NOT NULL,
    user_id character varying,
    name character varying NOT NULL,
    goal character varying NOT NULL,
    due date,
    hpw integer,
    progress double precision DEFAULT 0,
    status character varying(25) DEFAULT 'On Track'::character varying
);


ALTER TABLE public.roadmaps OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    uid character varying NOT NULL,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    email character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: certificates id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates ALTER COLUMN id SET DEFAULT nextval('public.certificates_id_seq'::regclass);


--
-- Data for Name: certificates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.certificates (id, user_id, name, issuer, issue_date, credential_id, category, url, file_url) FROM stdin;
1	u126	Example Certificate	TFP Corp	2025-04-12	S00001	Useless	\N	\N
\.


--
-- Data for Name: course_nodes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.course_nodes (id, name, link, status, avg_time_to_finish, roadmap_id, child_ids) FROM stdin;
j001	Advanced Java Collections	https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html	unfinished	15	r002	j002,j003
j002	Concurrency and Multithreading in Java	https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html	unfinished	20	r002	j004
j003	Design Patterns in Java	https://refactoring.guru/design-patterns/java	unfinished	25	r002	j005
j004	Java Memory Model	https://www.javatpoint.com/java-memory-management	unfinished	10	r002	
j005	SOLID Principles	https://www.digitalocean.com/community/conceptual_articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design	unfinished	12	r002	
j006	Spring Data JPA	https://spring.io/projects/spring-data-jpa	unfinished	18	r002	
j007	Spring Security	https://spring.io/projects/spring-security	unfinished	22	r002	
j008	Spring Boot Testing	https://spring.io/guides/gs/testing-web/	unfinished	15	r002	
cpp001	Advanced C++ Syntax and Features	https://www.udemy.com/course/advanced-cpp-programming/	unfinished	30	cpp_super_1	
cpp002	Memory Management and Smart Pointers	https://www.youtube.com/playlist?list=PL5jc9xFGsL8E12so1wlMS0r0hPQUsZ9yN	unfinished	25	cpp_super_1	
cpp003	Concurrency and Parallelism in C++	https://www.modernescpp.com/index.php/multithreading-and-concurrency-in-c-11-part-1	unfinished	35	cpp_super_1	
cpp004	RAII (Resource Acquisition Is Initialization)	https://en.cppreference.com/w/cpp/language/raii	unfinished	15	cpp_super_1	
cpp005	Custom Allocators	https://www.youtube.com/watch?v=hEx5JHxz2t0	unfinished	20	cpp_super_1	
cpp006	C++ Threading Library	https://www.tutorialspoint.com/cplusplus/cpp_multithreading.htm	unfinished	25	cpp_super_1	
cpp007	Design Patterns in C++	https://www.vincehuston.org/dp/	unfinished	40	cpp_super_1	
cpp008	C++ Build Systems (CMake)	https://cmake.org/cmake/help/latest/guide/tutorial/index.html	unfinished	15	cpp_super_1	
c001	Advanced Data Structures in C	https://www.udemy.com/course/data-structures-algorithms-c/	unfinished	20	rC001	c002,c003
c002	Memory Management and Pointers	https://www.youtube.com/playlist?list=PL2_aWCzGMAwI9HK8YPVBjElJ3_PwnouK6	unfinished	15	rC001	c004,c005
c003	Concurrency and Multithreading in C	https://www.amazon.com/dp/B07XYC7T2X	unfinished	25	rC001	c006,c007
c004	Dynamic Memory Allocation	https://www.geeksforgeeks.org/dynamic-memory-allocation-in-c/	unfinished	8	rC001	
c005	Pointer Arithmetic	https://www.tutorialspoint.com/cprogramming/c_pointer_arithmetic.htm	unfinished	7	rC001	
c006	Threads and Mutexes	https://www.youtube.com/watch?v=JqgDkt_10VA	unfinished	12	rC001	
c007	Inter-Process Communication	https://www.cs.rutgers.edu/~rmartin/teaching/fall13/439/slides/ipc.pdf	unfinished	13	rC001	
c008	C Coding Best Practices	https://www.amazon.com/dp/0596007011	unfinished	10	rC001	
cn_rm_u126__3ecae6fb	Introduction to Backend Development	https://www.udacity.com/course/full-stack-web-developer--nd0044	unfinished	4	rm_u126_3860aec6	cn_rm_u126__3d631e51,cn_rm_u126__663c7bb5
cn_rm_u126__3d631e51	Learn Git and Version Control	https://www.codecademy.com/learn/learn-git	unfinished	6	rm_u126_3860aec6	cn_rm_u126__378d23ac,cn_rm_u126__bdc41c4b
cn_rm_u126__663c7bb5	Basic Linux Command Line	https://www.udemy.com/course/linux-tutorial-for-beginners/	unfinished	4	rm_u126_3860aec6	cn_rm_u126__6817a994
cn_rm_u126__378d23ac	Introduction to Python	https://www.coursera.org/learn/python-for-everybody	unfinished	10	rm_u126_3860aec6	
cn_rm_u126__bdc41c4b	Introduction to JavaScript	https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures/	unfinished	12	rm_u126_3860aec6	
cn_rm_u126__6817a994	Networking Basics	https://www.cloudflare.com/learning/network-layer/what-is-networking/	unfinished	8	rm_u126_3860aec6	cn_rm_u126__3011ed89,cn_rm_u126__d9706f53
cn_rm_u126__3011ed89	HTTP Protocol	https://developer.mozilla.org/en-US/docs/Web/HTTP	unfinished	4	rm_u126_3860aec6	
cn_rm_u126__d9706f53	REST APIs	https://restfulapi.net/	unfinished	6	rm_u126_3860aec6	
cn_rm_u126__08ecb19a	HTML Fundamentals	https://www.codecademy.com/learn/learn-html	unfinished	15	rm_u126_750ee325	cn_rm_u126__2ea88675,cn_rm_u126__a9fc732a
cn_rm_u126__2ea88675	CSS Styling	https://www.freecodecamp.org/learn/2022/responsive-web-design/	unfinished	20	rm_u126_750ee325	cn_rm_u126__efc47c77,cn_rm_u126__7aa7034d
cn_rm_u126__a9fc732a	JavaScript Basics	https://www.codecademy.com/learn/introduction-to-javascript	unfinished	25	rm_u126_750ee325	cn_rm_u126__9e61a89b
cn_rm_u126__efc47c77	Responsive Design Principles	https://developer.mozilla.org/en-US/docs/Learn/CSS/Responsive_design	unfinished	10	rm_u126_750ee325	
cn_rm_u126__7aa7034d	CSS Frameworks (Bootstrap/Tailwind)	https://getbootstrap.com/docs/5.3/getting-started/introduction/	unfinished	18	rm_u126_750ee325	
cn_rm_u126__9e61a89b	DOM Manipulation	https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model/Introduction	unfinished	15	rm_u126_750ee325	cn_rm_u126__5c58476a,cn_rm_u126__da31abfc
cn_rm_u126__5c58476a	Version Control with Git	https://www.atlassian.com/git/tutorials/what-is-version-control	unfinished	12	rm_u126_750ee325	
cn_rm_u126__da31abfc	Asynchronous JavaScript and APIs	https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/async_function	unfinished	20	rm_u126_750ee325	
\.


--
-- Data for Name: roadmaps; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roadmaps (id, user_id, name, goal, due, hpw, progress, status) FROM stdin;
rm2024-01	u123	Java Mastery	Become an advanced Java developer	2025-12-31	10	0	On Track
cpp_master_001	u124	C++ Master	Become a Senior C++ developer	2025-12-31	10	0	On Track
cpp_master_1	u123	C++ Master	Become a Senior C++ developer	2025-12-31	10	0	On Track
cpp_super_1	u123	C++ Super	Become a Senior C++ developer	2025-12-31	10	0	On Track
c_super_r001	u123	C Super	Become a Senior C developer	2025-12-31	10	0	On Track
c_super_roadmap_001	u123	C Super	Become a Senior C developer	2025-12-31	10	0	On Track
c_super_001	u123	C Super	Become a Senior C developer	2025-12-31	10	0	On Track
rC001	u123	C Super	Become a Senior C developer	2025-12-31	10	0	On Track
rm103	u123	Data Analyst Path	Senior Data Analyst	2025-11-30	8	0	On Track
rm110	u123	Data Analyst Path	Senior Data Analyst	2025-11-30	8	\N	\N
r001	u125	First Step	Become a senior frontend Reactjs developer	2025-12-31	10	\N	\N
r002	u125	Second Step	Senior Reactjs Frontend developer	2025-11-30	8	\N	\N
rm_u126_3860aec6	u126	Manual Beginner Plan	Become Backend Developer	2025-12-31	8	\N	\N
rm_u126_750ee325	u126	Fullstack Bootcamp	Fullstack Developer	2025-12-31	12	\N	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (uid, first_name, last_name, email) FROM stdin;
u123	Thomas	Nguyen	tommynguyen@example.com
u124	ben	Jamin	benjamine@tpf.com
u125	Adopt	Herlit	adobe@dqx.com
u126	Jerry	Tomowner	jtommorow@jnt.com
\.


--
-- Name: certificates_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.certificates_id_seq', 1, false);


--
-- Name: certificates certificates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT certificates_pkey PRIMARY KEY (id);


--
-- Name: course_nodes course_nodes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_nodes
    ADD CONSTRAINT course_nodes_pkey PRIMARY KEY (id);


--
-- Name: roadmaps roadmaps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roadmaps
    ADD CONSTRAINT roadmaps_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (uid);


--
-- Name: certificates certificates_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT certificates_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(uid) ON DELETE CASCADE;


--
-- Name: course_nodes fk_course_nodes_roadmap; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_nodes
    ADD CONSTRAINT fk_course_nodes_roadmap FOREIGN KEY (roadmap_id) REFERENCES public.roadmaps(id) ON DELETE CASCADE;


--
-- Name: roadmaps roadmaps_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roadmaps
    ADD CONSTRAINT roadmaps_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(uid) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

