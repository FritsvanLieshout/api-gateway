package com.kwetter.frits.apigateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                    .route("user-service", r -> r.path("/api/user/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("http://localhost:8069"))
                    .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("http://localhost:8067"))
                .build();
    }
}
