package com.kwetter.frits.apigateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.header}")
    private String header;

    public String getSecret() {
        return secret;
    }

    public String getHeader() {
        return header;
    }
}
