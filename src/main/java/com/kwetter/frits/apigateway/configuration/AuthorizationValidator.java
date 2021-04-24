package com.kwetter.frits.apigateway.configuration;

import com.kwetter.frits.apigateway.entity.EndpointAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class AuthorizationValidator {

    public static final List<EndpointAuthority> endpointAuthorities = List.of(
            new EndpointAuthority("/api/user/status", "user:read"),
            new EndpointAuthority("/api/timeline/all", "timeline:read")
    );

    public Boolean checkAuthority(String endpoint, Set<SimpleGrantedAuthority> authorities) {
        for (EndpointAuthority endpointAuthority : endpointAuthorities) {
            if (endpoint.equals(endpointAuthority.getEndpoint())) {
                if (!authorities.isEmpty()) {
                    if (authorities.contains(new SimpleGrantedAuthority(endpointAuthority.getAuthority()))) return true;
                }
            }
        }
        return false;
    }
}
