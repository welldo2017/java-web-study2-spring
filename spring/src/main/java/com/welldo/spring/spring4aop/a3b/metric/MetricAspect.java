package com.welldo.spring.spring4aop.a3b.metric;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * author:welldo
 * date: 2022-02-20 20:16
 */
@Aspect
@Component
public class MetricAspect {

    /**
     * 注意,metric()方法标注了: @Around("@annotation(metricTime)")
     * 它的意思是，符合条件的目标方法是带有@MetricTime注解的方法，
     *
     * 因为metric()方法参数类型是MetricTime（注意参数名是metricTime不是MetricTime），
     * 我们通过它获取性能监控的名称。
     *
     * 注意,
     * 1. @Around 注解代表该切面具备在方法前后均执行操作的能力，
     * 但具体是否在方法前后都执行,还要取决于 pjp.proceed() 方法的前后是否都写了自定义的逻辑。
     * 该示例中只在 pjp.proceed() 的后面写了打印语句，因此只会有一条打印语句。
     *
     * 2. @Around("@annotation(xxx)"),这个xxx,必须与方法签名里annotation类里的参数名一模一样,不然会报错
     */
    @Around("@annotation(aaa)")
    public Object metric(ProceedingJoinPoint joinPoint, MetricTime aaa) throws Throwable {
        //获取方法名
        String name = aaa.value();

        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long t = System.currentTimeMillis() - start;
            // 写入日志或发送至JMX:
            System.err.println("[Metrics] " + name + ": " + t + "ms");
        }
    }
}
