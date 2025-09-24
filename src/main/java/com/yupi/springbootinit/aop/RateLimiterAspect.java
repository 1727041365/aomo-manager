package com.yupi.springbootinit.aop;

import com.yupi.springbootinit.annotation.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Aspect
@Component
public class RateLimiterAspect {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate; // 修改为Object类型

    @Around("@annotation(rateLimiter)")
    public Object around(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String methodName = point.getSignature().getName();
        String key = "rate_limit:" + ip + ":" + methodName;

        // 类型转换
        Object countObj = redisTemplate.opsForValue().get(key);
        Integer count = countObj != null ? Integer.valueOf(countObj.toString()) : null;

        if (count == null) {
            redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(rateLimiter.timeout()));
        } else if (count < rateLimiter.limit()) {
            redisTemplate.opsForValue().increment(key);
        } else {
            throw new RuntimeException("Access too frequently, please try again later");
        }
        return point.proceed();
    }
}
