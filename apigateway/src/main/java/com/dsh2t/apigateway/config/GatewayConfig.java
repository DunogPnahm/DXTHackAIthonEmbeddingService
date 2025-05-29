package com.dsh2t.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthFilter authFilter;

    public GatewayConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        // Kiểm tra token
//        if (token == null || !token.startsWith("Bearer ")) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
        return builder.routes()
                // Chatroom Service Route
                .route("chatroom-service", r -> r.path("/chatroom/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config()))
                                .addRequestHeader("Authorization", "Bearer your-auth-token")) // Thêm Authorization Header
                        .uri("http://localhost:8080")) // Backend URL

                // Another Service Route
                .route("nlp-to-sql-service", r -> r.path("/nlptosqlservice/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://127.0.0.1:5000")) // Replace with the actual URL of another service

                // Third Service Route
                .route("user-service", r -> r.path("/userservice/**")
                        .uri("http://localhost:8081")) // Replace with the actual URL of the third service
                .build();
    }
}
