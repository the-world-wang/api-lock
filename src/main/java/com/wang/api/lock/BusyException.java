package com.wang.api.lock;

/**
 * Created by paopao on 2017/7/8.
 */
public class BusyException extends RuntimeException {

    public BusyException() {
        super("系统繁忙");
    }
}
