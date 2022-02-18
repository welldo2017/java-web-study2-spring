package com.welldo.spring3txt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 在Java程序中，我们经常会读取配置文件、资源文件等。
 * 使用Spring容器时，我们也可以把“文件”注入进来，方便程序读取。
 *
 * Spring提供了一个org.springframework.core.io.Resource（注意不是javax.annotation.Resource），
 * 它可以像String、int一样使用@Value注入：
 *
 * author:welldo
 * date: 2022-02-18 13:19
 */
@ComponentScan
@Configuration
public class Main {
    static ApplicationContext context = null;



    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Resources appService = context.getBean(Resources.class);
        appService.printLogo();
    }

}
