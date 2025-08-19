package com.muyategna.backend.billing.gateways.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PesapalTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${pesapal.access-token-key}")
    private String pesapalAccessTokenKey;

    public PesapalTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String token) {
        // Save token with 5 minutes expiration
        redisTemplate.opsForValue().set(pesapalAccessTokenKey, token, Duration.ofMinutes(4).plusSeconds(30));
    }

    public String getToken() {
        return redisTemplate.opsForValue().get(pesapalAccessTokenKey);
    }
}

