package com.dsh2t.apigateway.config;

import com.dsh2t.apigateway.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {


    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
        System.out.println("AuthFilter created!");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("AuthFilter applied!");
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing Authorization header");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts = authHeader.split(" ");
            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect Authorization header");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://apigateway/userservice/validateToken?token=" + parts[1])
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve().bodyToMono(UserDto.class)
                    .map(userDto -> {
                        exchange.getRequest().mutate()
                                .header("X-User-Id", String.valueOf(userDto.getUserId()))
                                .header(HttpHeaders.AUTHORIZATION, authHeader);
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {
        // Put configuration properties here
    }

}
