package com.mrer.movie.streaming.service.secur;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Base64;

@Component
public class SignedUrlGenerator {
    @Value("${streaming.cdn.base-url}")
    private String cdnBaseUrl;

    @Value("${streaming.secret}")
    private String secret;

    public String generate(String movieId, Duration ttl) {

        long expires = System.currentTimeMillis() + ttl.toMillis();
        String path = "/hls/" + movieId + "/master.m3u8";

        String data = path + expires;
        String signature = hmacSha256(data, secret);

        return cdnBaseUrl + path +
                "?expires=" + expires +
                "&signature=" + signature;
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Signing failed", e);
        }
    }
}
