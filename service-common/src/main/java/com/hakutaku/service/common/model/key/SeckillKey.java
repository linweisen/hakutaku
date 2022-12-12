package com.hakutaku.service.common.model.key;

public class SeckillKey extends BaseKey {

    public static SeckillKey KEY = new SeckillKey(7200,"SECKILL");

    public static SeckillKey COUNT = new SeckillKey(7200,"SECKILL_COUNT");

    public static SeckillKey ORDER = new SeckillKey(-1,"SECKILL_ORDER");

    public static SeckillKey BEFORE_PAY = new SeckillKey(-1,"SECKILL_BEFORE_PAY");

//    public static SeckillKey ORDER = new SeckillKey(-1,"SECKILL_");

    private SeckillKey(int expires, String prefix) {
        super(expires, prefix);
    }
}
