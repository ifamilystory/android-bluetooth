package com.zhangling.bluetooth.model;

/**
 * Created by ysec on 2018/3/19.
 */

public class RxBusBaseMessage {
    private int code;
    private Object object;

    public RxBusBaseMessage(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public RxBusBaseMessage() {

    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}
