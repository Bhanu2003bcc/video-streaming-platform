package com.mrer.movie.streaming.service.secur;

import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Component
public class StreamingPermissionChecker {
    public void check(UUID movieId, String userId, String role) throws AccessDeniedException {

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
