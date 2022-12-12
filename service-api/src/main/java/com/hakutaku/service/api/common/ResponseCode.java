package com.hakutaku.service.api.common;

public enum ResponseCode {

    SUCCESS(200, "操作成功"),

    REQUEST_FAILD(500, "请求失败"),

    E_20011(20011, "缺少必填参数"),

    PAY_E_NON_ORDER(20001, "订单不存在"),

    PAY_E_REPEAT(20002, "重复支付"),

    PAY_E_ERROR(20003, "支付失败"),

    ORDER_E_UPDATE_FAILD(20101, "订单更新失败"),

    ORDER_E_CANCEL_FAILD(20102, "订单取消失败"),

    ORDER_E_PAID(20103, "订单已付款"),

    SECKILL_E_START(30001, "秒杀活动已开始"),

    SECKILL_E_NOT_ENOUGH(30002, "已售完"),

    E_99999(99999, "default");

    private Integer code;

    private String msg;

    ResponseCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
