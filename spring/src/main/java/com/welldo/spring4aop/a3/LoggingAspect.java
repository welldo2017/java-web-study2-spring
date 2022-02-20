package com.welldo.spring4aop.a3;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 3.
 * 使用AspectJ实现AOP比较方便，因为它的定义比较简单。
 *
 * AspectJ的注入语法则比较复杂,请参考
 * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-pointcuts-examples
 * 略懂即可.
 * 因为,在实际项目中，这种写法其实很少使用
 *
 * author:welldo
 * date: 2022-02-20 18:08
 */
@Component
@Aspect //需要加上这个注解,并且我们需要给@Configuration类加上一个@EnableAspectJAutoProxy注解：
public class LoggingAspect {

    //执行UserService的每个public方法前执行 doAccessCheck()代码。
    @Before("execution(public * com.welldo.spring4aop.a3.service.UserService.*(..))")//不限返回值,不限参数个数
    public void doAccessCheck() {
        System.err.println("[Before] do access check...");
    }

    //它和@Before不同，@Around可以决定是否执行目标方法，
    // 在执行MailService的每个方法前后,都执行:
    @Around("execution(public * com.welldo.spring4aop.a3.service.MailService.*(..))")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {

        System.err.println("[Around-1] start:签名= " + pjp.getSignature());//目标方法的签名

        Object retVal = pjp.proceed();//执行目标方法,也可以注释这行,不执行目标方法.

        System.err.println("[Around-2] done:签名= " + pjp.getSignature());
        return retVal;
    }
}
