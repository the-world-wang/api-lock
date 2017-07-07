package com.wang.api.lock;

/**
 * Created by paopao on 2017/7/4.
 */
public interface ApiLockService {

    void tryLock(String key) throws BusyException;

    void unlock(String key);
}
