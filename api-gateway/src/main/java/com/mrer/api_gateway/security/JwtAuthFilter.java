package com.mrer.api_gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered{

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final SecretKey secretKey;

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();

        // ✅ Public endpoints
        if (path.startsWith("/auth")
                || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            // 🔐 Remove spoofed headers + inject trusted ones
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .headers(headers -> {
                        headers.remove("X-USER-ID");
                        headers.remove("X-USER-ROLE");
                    })
                    .header("X-USER-ID", userId)
                    .header("X-USER-ROLE", role)
                    .build();

            log.info("Authenticated request userId={}, role={}", userId, role);

            return chain.filter(
                    exchange.mutate().request(mutatedRequest).build()
            );

        } catch (Exception ex) {
            log.error("JWT validation failed", ex);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    /**
     * Ensure this filter runs BEFORE routing
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
