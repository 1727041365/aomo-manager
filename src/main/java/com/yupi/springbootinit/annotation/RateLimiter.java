package com.yupi.springbootinit.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 5; // 默认限制次数
    int timeout() default 60; // 默认超时时间(秒)
}
