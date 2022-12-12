package com.hakutaku.service.common.config.security;

import com.hakutaku.service.common.model.User;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;

public interface TokenResolvable {

    Boolean validateToken(String token);

    String getToken(HttpServletRequest request);

    String refreshToken(String token);

    int getExpiredIn(Device device);

    String generateToken(Long userId, String username, Device device);

    String getUsernameFromToken(String token);

    Long getUserIdFromToken(String token);

    User getUserFromToken(String token);
}
