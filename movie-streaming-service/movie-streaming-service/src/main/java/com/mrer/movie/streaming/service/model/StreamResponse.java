package com.mrer.movie.streaming.service.model;

import java.time.LocalDateTime;

public record StreamResponse(
        String streamUrl,
        LocalDateTime expiresAt) {}
