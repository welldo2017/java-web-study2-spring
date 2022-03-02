package com.welldo.spring.spring4aop.a4;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 避坑指南
 *
 * 使用注解既简单，又能明确标识AOP装配，是使用AOP推荐的方式。
 *
 * 总结:无论是使用AspectJ语法，还是配合Annotation，
 * 使用AOP，实际上就是让Spring自动为我们创建一个Proxy，使得调用方能无感知地调用指定方法，但运行期却动态“织入”了其他逻辑，
 * 因此，AOP本质上就是一个代理模式。
 *
 * author:welldo
 * date: 2022-02-21 20:16
 */
@EnableAspectJAutoProxy
@Configuration
@ComponentScan
public class AppConfig {


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MailService mailService = context.getBean(MailService.class);


        //todo 后面加注释
        //不加aop的情况下,一切正常
        //加上aop,再在本类加上 @EnableAspectJAutoProxy , 应该会得到 npe
        System.out.println(mailService.sendMail());
    }
}
