package com.hakutaku.service.common.model.key;


import com.hakutaku.service.common.constant.SymbolConstants;

public abstract class BaseKey {

    private int expires;

    private String prefix;

    public BaseKey(String prefix){
        this(0, prefix);
    }

    public BaseKey(int expires, String prefix){
        this.expires = expires;
        this.prefix = prefix;
    }

    public int expires() {
        return expires;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + SymbolConstants.COLON + prefix + SymbolConstants.COLON;
    }

    public String getKey() {
        String className = getClass().getSimpleName();
        return className + SymbolConstants.COLON + prefix;
    }
}
