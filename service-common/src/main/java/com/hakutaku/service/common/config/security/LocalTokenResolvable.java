package com.hakutaku.service.common.config.security;


public class LocalTokenResolvable extends AbstractTokenResolvable {

    public LocalTokenResolvable(String APP_NAME, String SECRET, int EXPIRES_IN, int MOBILE_EXPIRES_IN, String AUTH_HEADER) {
        super(APP_NAME, SECRET, EXPIRES_IN, MOBILE_EXPIRES_IN, AUTH_HEADER);
    }

}
