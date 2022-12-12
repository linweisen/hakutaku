package com.hakutaku.service.api.controller;

import com.hakutaku.service.api.service.AuthenticationService;
import com.hakutaku.service.common.config.security.auth.JwtAuthenticationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "权限接口")
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;



    @ApiOperation("登陆")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = false, dataType = "String", paramType="header")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Validated @RequestBody JwtAuthenticationRequest authenticationRequest,
            Device device) throws AuthenticationException {

        return ResponseEntity.ok(authenticationService.login(authenticationRequest, device));
    }


}