package com.mrer.movie.streaming.service.service;

import com.mrer.movie.streaming.service.model.StreamResponse;
import com.mrer.movie.streaming.service.secur.SignedUrlGenerator;
import com.mrer.movie.streaming.service.secur.StreamingPermissionChecker;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StreamingAccessService {

    private final StreamingPermissionChecker permissionChecker;
    private final SignedUrlGenerator signedUrlGenerator;

    public StreamingAccessService(
            StreamingPermissionChecker permissionChecker,
            SignedUrlGenerator signedUrlGenerator) {
        this.permissionChecker = permissionChecker;
        this.signedUrlGenerator = signedUrlGenerator;
    }

    public StreamResponse generateStreamUrl(
            UUID movieId,
            String userId,
            String role) throws AccessDeniedException {

        permissionChecker.check(movieId, userId, role);

        String signedUrl =
                signedUrlGenerator.generate(String.valueOf(movieId), Duration.ofMinutes(10));

        return new StreamResponse(
                signedUrl,
                LocalDateTime.now().plusMinutes(10)
        );
    }
}
