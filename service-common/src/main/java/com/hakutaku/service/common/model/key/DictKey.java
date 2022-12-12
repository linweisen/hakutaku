package com.hakutaku.service.common.model.key;

public class DictKey extends BaseKey {

    public static DictKey KEY = new DictKey(-1,"DICT");

    public DictKey(int expires, String prefix) {
        super(expires, prefix);
    }
}
