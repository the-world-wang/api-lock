package com.wang.api.lock;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by paopao on 2017/7/4.
 */
public class ApiLockServiceImpl implements ApiLockService {

    private static final String LOCK_VALUE = "lock";

    private StringRedisTemplate redisTemplate;
    private int retryTimes = 3;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public void tryLock(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            throw new BusyException();
        }
        redisTemplate.opsForValue().set(key, LOCK_VALUE);
    }

    @Override
    public void unlock(String key) {
        for (int i = 0; i < retryTimes; i++) {
            if (delete(key)) break;
        }
    }

    private boolean delete(String key) {
        redisTemplate.delete(key);
        return redisTemplate.opsForValue().get(key) == null;
    }
}
