package com.kwetter.frits.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().configurationSource(configCors())
                .and()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.GET, "/api/timeline/**").hasAuthority("timeline:read")
                    .pathMatchers(HttpMethod.GET, "/api/tweets/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/tweets/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    @Bean
    public CorsConfigurationSource configCors() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "UPDATE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000"); //https://kwetterplatform.azurewebsites.net/
        configuration.setMaxAge(3600L);
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("*");
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
