package com.hakutaku.service.api.utils;

public class RequestIdContextHolder {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private RequestIdContextHolder(){}

    public static void setRequestId(String id) {
        threadLocal.set(id);
    }

    public static String getRequestId(){
        return threadLocal.get();
    }

}
