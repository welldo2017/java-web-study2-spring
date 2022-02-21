package com.welldo.spring4aop.a3b;

import com.welldo.spring4aop.a3b.metric.MetricTime;
import com.welldo.spring4aop.a3b.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 使用注解装配AOP
 *
 * 1.
 * 上一节我们讲解了使用AspectJ的注解，并配合一个复杂的execution(* xxx.Xyz.*(..))语法来定义应该如何装配AOP。
 * 在实际项目中，这种写法其实很少使用
 *
 * 示例a: @Before("execution(public * com.itranswarp.learnjava.service.*.*(..))")
 * 基本能实现无差别全覆盖，即某个包下面的所有Bean的所有方法都会被这个check()方法拦截。
 *
 * 示例b: @Around("execution(public * update*(..))")
 * 这种非精准打击误伤面更大，因为从 "方法前缀" 区分是否是数据库操作是非常不可取的。
 *
 * 总结:
 * 自动装配时，因为不恰当的范围，容易导致意想不到的结果，
 * 即很多不需要AOP代理的Bean也被自动代理了，
 * 并且，后续新增的Bean，如果不清楚现有的AOP装配规则，容易被强迫装配。
 *
 * 2. * 装配AOP的时候，使用注解是最好的方式
 * 举例 {@link MetricTime}
 *
 * author:welldo
 * date: 2022-02-20 17:09
 */


@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService bean = context.getBean(UserService.class);
        bean.register("ss","ss","ss");

    }




}
