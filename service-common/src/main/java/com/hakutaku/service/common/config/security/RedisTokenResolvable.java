package com.hakutaku.service.common.config.security;

import com.hakutaku.service.common.model.key.UserKey;
import io.jsonwebtoken.Jwts;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mobile.device.Device;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisTokenResolvable extends AbstractTokenResolvable {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisTokenResolvable(String APP_NAME, String SECRET, int EXPIRES_IN, int MOBILE_EXPIRES_IN, String AUTH_HEADER) {
        super(APP_NAME, SECRET, EXPIRES_IN, MOBILE_EXPIRES_IN, AUTH_HEADER);
    }

    @Override
    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        String cacheToken = (String) redisTemplate.opsForList().index(UserKey.KEY.getPrefix() + username, 1);
        return username != null && token.equals(cacheToken);
    }

    @Override
    public String refreshToken(String token) {
        final String username = getUsernameFromToken(token);
        redisTemplate.expire(UserKey.KEY.getPrefix() + username, UserKey.KEY.expires(), TimeUnit.SECONDS);
        return token;
    }

    @Override
    public String generateToken(Long userId, String username, Device device) {
        String audience = generateAudience(device);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setId(String.valueOf(userId))
                .setSubject(username)
                .setAudience(audience)
                .setIssuedAt(new Date())
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
