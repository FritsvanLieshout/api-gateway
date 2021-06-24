package com.kwetter.frits.apigateway.configuration;

import com.kwetter.frits.apigateway.entity.EndpointAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class AuthorizationValidator {

    private static final String USER_READ = "user:read";
    private static final String TIMELINE_READ = "timeline:read";
    private static final String ROLE_USER = "ROLE_KWETTER_USER";
    private static final String ROLE_ADMIN = "ROLE_KWETTER_ADMIN";

    public static final List<EndpointAuthority> endpointAuthorities = List.of(
            new EndpointAuthority("/api/user/status", USER_READ),
            new EndpointAuthority("/api/user/me", USER_READ),
            new EndpointAuthority("/api/user/edit", "user:update"),
            new EndpointAuthority("/api/user/permanent/remove", "user:delete"),
            new EndpointAuthority("/api/timeline/all", TIMELINE_READ),
            new EndpointAuthority("/api/timeline/unique", TIMELINE_READ),
            new EndpointAuthority("/api/timeline/own/tweets", TIMELINE_READ),
            new EndpointAuthority("/api/tweets/tweet", "tweet:write"),
            new EndpointAuthority("/api/tweets/mentions", "tweet:read"),
            new EndpointAuthority("/api/follow/user/follow", ROLE_USER),
            new EndpointAuthority("/api/follow/user/unfollow", ROLE_USER),
            new EndpointAuthority("/api/follow/following", ROLE_USER),
            new EndpointAuthority("/api/follow/followers", ROLE_USER),
            new EndpointAuthority("/api/auth/logout", "user:logout"),
            new EndpointAuthority("/api/trending/items", USER_READ),
            new EndpointAuthority("/api/like/add", ROLE_USER),
            new EndpointAuthority("/api/like/remove", ROLE_USER),
            new EndpointAuthority("/api/like/tweet", ROLE_USER),
            new EndpointAuthority("/api/like/user", ROLE_USER),
            new EndpointAuthority("/api/like/user/likes", ROLE_USER),
            new EndpointAuthority("/api/moderation/users/all", ROLE_ADMIN),
            new EndpointAuthority("/api/moderation/logs", ROLE_ADMIN),
            new EndpointAuthority("/actuator/gateway/routes", ROLE_ADMIN)
    );

    public Boolean checkAuthority(String endpoint, Set<SimpleGrantedAuthority> authorities) {
        for (EndpointAuthority endpointAuthority : endpointAuthorities) {
            if (endpoint.equals(endpointAuthority.getEndpoint()) && !authorities.isEmpty() && authorities.contains(new SimpleGrantedAuthority(endpointAuthority.getAuthority()))) return true;
        }
        return false;
    }
}
