package com.hakutaku.service.api.service.impl;

import com.hakutaku.service.api.mapper.UserMapper;
import com.hakutaku.service.api.model.UserTokenState;
import com.hakutaku.service.api.service.AuthenticationService;
import com.hakutaku.service.api.service.BaseService;
import com.hakutaku.service.common.config.security.auth.JwtAuthenticationRequest;
import com.hakutaku.service.common.model.User;
import com.hakutaku.service.common.model.key.UserKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class AuthenticationServiceImpl extends BaseService implements UserDetailsService, AuthenticationService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        return user;
    }


    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest, Device device) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword())
        );
        // Inject into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String jws;
        int expiresIn;
        User cacheUser = (User) redisTemplate.opsForList().index(UserKey.KEY.getPrefix() + authenticationRequest.getUsername(), 0);
        if (null == cacheUser) {
            redisTemplate.opsForList().leftPush(UserKey.KEY.getPrefix() + user.getUsername(), user);
            jws = tokenResolvable.generateToken(user.getId(), user.getUsername(), device);
            expiresIn = tokenResolvable.getExpiredIn(device);
            redisTemplate.opsForList().rightPush(UserKey.KEY.getPrefix() + user.getUsername(), jws);

        }else {
            jws = (String) redisTemplate.opsForList().index(UserKey.KEY.getPrefix() + user.getUsername(), 1);
            expiresIn = tokenResolvable.getExpiredIn(device);
        }
        redisTemplate.expire(UserKey.KEY.getPrefix() + user.getUsername(), UserKey.KEY.expires(), TimeUnit.SECONDS);
        return new UserTokenState(jws, expiresIn);
    }
}
