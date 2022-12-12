package com.hakutaku.service.common.model.key;

public class UserKey extends BaseKey {

    public static final int USER_EXPIRE = 7200;

    public static UserKey KEY = new UserKey(USER_EXPIRE,"USER");

    public static UserKey USER_BEFORE_PAY = new UserKey(-1, "USER_BEFORE_PAY");

    private UserKey(int expires, String prefix) {
        super(expires, prefix);
    }
}
