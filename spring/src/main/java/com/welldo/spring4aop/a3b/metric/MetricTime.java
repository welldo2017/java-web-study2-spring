package com.welldo.spring4aop.a3b.metric;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 我们以一个实际例子演示如何使用注解实现AOP装配。
 * 为了监控应用程序的性能，我们定义一个性能监控的注解：
 *
 * 1.在需要被监控的关键方法上标注该注解 {@link com.welldo.spring4aop.a3b.service.UserService}
 * 2.编写一个切面 {@link MetricAspect}
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface MetricTime {

    String value();
}
