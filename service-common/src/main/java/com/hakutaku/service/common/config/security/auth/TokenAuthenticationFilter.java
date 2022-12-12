package com.hakutaku.service.common.config.security.auth;

import com.hakutaku.service.common.config.security.TokenResolvable;
import com.hakutaku.service.common.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    private TokenResolvable tokenResolvable;


    public TokenAuthenticationFilter(TokenResolvable tokenResolvable) {
        this.tokenResolvable = tokenResolvable;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authToken = tokenResolvable.getToken(request);

        if (authToken != null) {
            if (tokenResolvable.validateToken(authToken)) {
                tokenResolvable.refreshToken(authToken);
                User user = tokenResolvable.getUserFromToken(authToken);
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(user);
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

}