package com.wang.api.lock;

import java.lang.annotation.*;

/**
 * Created by paopao on 2017/7/4.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLock {

    /**
     * session的表达式,支持SpEL表达式
     */
    String session();

    /**
     * 在request里面对应attribute的名称
     */
    String attribute();
}
