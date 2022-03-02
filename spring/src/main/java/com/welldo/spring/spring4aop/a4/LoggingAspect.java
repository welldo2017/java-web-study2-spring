package com.welldo.spring.spring4aop.a4;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * author:welldo
 * date: 2022-02-21 10:38
 */

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(public * com.welldo.spring.spring4aop.a4.UserService.*(..))")
    public void doAccessCheck() {
        System.err.println("[Before] do access check...");
    }
}
