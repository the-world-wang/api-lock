package com.wang.api.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by paopao on 2017/7/4.
 */
@Configuration
@Aspect
@EnableConfigurationProperties(ApiLockProperties.class)
@ConditionalOnClass(ApiLockService.class)
@ConditionalOnProperty(prefix = "com.wang.api.lock", value = "enable", matchIfMissing = true)
public class ApiLockConfiguration {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ApiLockProperties apiLockProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private ApiLockService apiLockService;
    private String prefix;

    @PostConstruct
    public void init() {
        ApiLockServiceImpl apiLockService = new ApiLockServiceImpl();
        apiLockService.setRedisTemplate(redisTemplate);
        apiLockService.setRetryTimes(apiLockProperties.getRetryTimes());
        this.apiLockService = apiLockService;
        prefix = apiLockProperties.getPrefix();
    }

    @Around("@annotation(com.wang.api.lock.ApiLock)")
    public Object tryLock(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        ApiLock apiLock = method.getAnnotation(ApiLock.class);
        String session = apiLock.session();
        String attribute = apiLock.attribute();
        String module = StringUtils.isEmpty(apiLock.module())
                ? method.getName() : apiLock.module();

        Object target = request.getAttribute(attribute);

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(session);
        String key = expression.getValue(target, String.class);

        String lock = prefix + module + key;
        boolean busy = false;
        try {
            apiLockService.tryLock(lock);
            return pjp.proceed();
        } catch (BusyException ex) {
            busy = true;
            throw ex;
        } finally {
            if (!busy) {
                apiLockService.unlock(lock);
            }
        }
    }
}
