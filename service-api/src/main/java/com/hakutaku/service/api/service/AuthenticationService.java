package com.hakutaku.service.api.service;

import com.hakutaku.service.api.model.UserTokenState;
import com.hakutaku.service.common.config.security.auth.JwtAuthenticationRequest;
import org.springframework.mobile.device.Device;

public interface AuthenticationService {

    UserTokenState login(JwtAuthenticationRequest authenticationRequest, Device device);
}
