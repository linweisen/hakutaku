package com.hakutaku.service.common.model.key;

public class OrderKey extends BaseKey {

    public static OrderKey KEY = new OrderKey(-1,"ORDER");

    private OrderKey(int expires, String prefix) {
        super(expires, prefix);
    }
}
