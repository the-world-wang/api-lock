package com.wang.api.lock;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by paopao on 2017/7/4.
 */
@ConfigurationProperties(prefix = "com.wang.api.lock")
public class ApiLockProperties {

    private String type = "redis";
    private boolean enable;
    private int concurrency;
    private String prefix = "api-lock:";
    private int retryTimes = 3;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
