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
                    .route("tweet-service", r -> r.path("/api/tweets/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://tweet-service"))
                    .route("user-service", r -> r.path("/api/user/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://user-service"))
                    .route("timeline-service", r -> r.path("/api/timeline/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://timeline-service"))
                    .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://auth-service"))
                    .route("follow-service", r -> r.path("/api/follow/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://follow-service"))
                    .route("trending-service", r -> r.path("/api/trending/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://trending-service"))
                    .route("like-service", r -> r.path("/api/like/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://like-service"))
                .build();
    }
}
