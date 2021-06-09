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
                    .route("tweet-service-service", r -> r.path("/api/tweets/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://tweet-service-service"))
                    .route("user-service-service", r -> r.path("/api/user/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://user-service-service"))
                    .route("timeline-service-service", r -> r.path("/api/timeline/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://timeline-service-service"))
                    .route("auth-service-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://auth-service-service"))
                    .route("follow-service-service", r -> r.path("/api/follow/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://follow-service-service"))
                    .route("trending-service-service", r -> r.path("/api/trending/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://trending-service-service"))
                    .route("like-service-service", r -> r.path("/api/like/**")
                        .filters(f -> f.filter(authenticationFilter))
                            .uri("lb://like-service-service"))
                .build();
    }
}
