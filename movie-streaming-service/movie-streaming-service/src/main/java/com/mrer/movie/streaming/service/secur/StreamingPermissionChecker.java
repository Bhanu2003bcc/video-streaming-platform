package com.mrer.movie.streaming.service.secur;

import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class StreamingPermissionChecker {
    public void check(String movieId, String userId, String role) throws AccessDeniedException {

        if (userId == null || userId.isBlank()) {
            throw new AccessDeniedException("Unauthenticated");
        }

        // Later:
        // - subscription
        // - geo
        // - parental control
        // - entitlement checks
    }
}
