package com.kwetter.frits.apigateway.configuration;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private RouterValidator routerValidator;

    @Autowired
    private AuthorizationValidator authorizationValidator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            String token = null;
            if (cookies != null && cookies.size() > 0) {
                List<HttpCookie> cookieList = cookies.get(jwtUtil.getHeader());

                if (!cookieList.isEmpty()) {
                    for (HttpCookie cookie : cookieList) {
                        if (Strings.isNullOrEmpty(cookie.getValue()) && !cookie.getName().equals(jwtUtil.getHeader())) {
                            return chain.filter(exchange);
                        } else {
                            token = cookie.getValue();
                        }
                    }

                    //Make a private method for this
                    try {
                        var signatureAlgorithm = SignatureAlgorithm.HS256;
                        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtUtil.getSecret());
                        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

                        Jws<Claims> claimsJws = Jwts.parser()
                                .setSigningKey(signingKey)
                                .parseClaimsJws(token);

                        var body = claimsJws.getBody();
                        String username = body.getSubject();
                        var authorities = (List<Map<String, String>>) body.get("authorities");
                        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                                .collect(Collectors.toSet());

                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                simpleGrantedAuthorities
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        if (!authorizationValidator.checkAuthority(request.getURI().getPath(), simpleGrantedAuthorities)) {
                            return onError(exchange, "Forbidden", HttpStatus.FORBIDDEN);
                        }

                    } catch (JwtException e) {
                        return onError(exchange, String.format("Token %s cannot be trusted / is expired", token), HttpStatus.UNAUTHORIZED);
                    }
                    return chain.filter(exchange);
                }
                return onError(exchange, "Cookie is missing", HttpStatus.UNAUTHORIZED);
            }
            return onError(exchange, "Cookie is missing", HttpStatus.UNAUTHORIZED);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
