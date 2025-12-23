package com.mrer.Auth_service.util;

import com.mrer.Auth_service.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user){
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expiration);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name()) // enum → string
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + expiration * 1000)
                )
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

    }
}
