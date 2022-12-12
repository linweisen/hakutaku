package com.hakutaku.service.api.config;

import com.hakutaku.service.common.config.security.RedisTokenResolvable;
import com.hakutaku.service.common.config.security.TokenResolvable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class BeanInjectionConfig {

    @Value("${app.name}")
    private String APP_NAME;

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.mobile_expires_in}")
    private int MOBILE_EXPIRES_IN;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    @Bean
    public TokenResolvable tokenResolvable(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        RedisTokenResolvable redisTokenResolvable = new RedisTokenResolvable(APP_NAME, SECRET, EXPIRES_IN, MOBILE_EXPIRES_IN, AUTH_HEADER);
        redisTokenResolvable.setRedisTemplate(redisTemplate);
        return redisTokenResolvable;
    }
}
