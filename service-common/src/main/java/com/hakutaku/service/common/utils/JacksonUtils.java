package com.hakutaku.service.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <V> V from(String json, Class<V> c) {
        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
            //log.error("jackson from error, json: {}, type: {}", json, c, e);
            return null;
        }
    }

    public static String toJsonString(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
