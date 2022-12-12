package com.hakutaku.service.api.controller;

import com.hakutaku.service.api.common.SymbolConstants;
import com.hakutaku.service.api.utils.RequestIdContextHolder;
import com.hakutaku.service.common.config.security.TokenResolvable;
import com.hakutaku.service.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public abstract class BaseController {

    @Autowired
    protected TokenResolvable tokenResolvable;

    protected User getUserFromToken(String token) {
        return tokenResolvable.getUserFromToken(token);
    }

    protected String generateRequestId(String token) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(UUID.randomUUID().toString())
                .append(SymbolConstants.COLON)
                .append(getUserFromToken(token).getId())
                .append(SymbolConstants.COLON)
                .append(request.getRequestURI())
                .append(SymbolConstants.COLON)
                .append(System.currentTimeMillis());
        RequestIdContextHolder.setRequestId(idBuilder.toString());

        return idBuilder.toString();
    }
}
