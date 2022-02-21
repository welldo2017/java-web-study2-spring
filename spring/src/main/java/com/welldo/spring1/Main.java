package com.welldo.spring1;

import com.welldo.spring1.bean.User;
import com.welldo.spring1.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 教程地址: https://www.liaoxuefeng.com/wiki/1252599548343744/1282382145519649
 * 那么到底如何使用IoC容器？装配好的Bean又如何使用？
 *
 * author:welldo
 * date: 2022-02-18 13:19
 */
public class Main {

    public static void main(String[] args) {

        /**
         * 最后一步，我们需要创建一个Spring的IoC容器实例，
         * 这个Spring容器会加载配置文件, 并为我们创建并装配好 xml 中指定的所有Bean.
         *
         * 可以看到，Spring容器就是ApplicationContext
         * 有很多实现类，这里我们选择ClassPathXmlApplicationContext，表示它会自动从classpath中查找指定的XML配置文件。
         * ps：ApplicationContext 会一次性创建所有的Bean，通常情况下，我们总是使用ApplicationContext
         */
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        // 获取Bean:
        // 获得了ApplicationContext的实例，就获得了IoC容器的引用。
        // 从ApplicationContext中我们可以根据Bean的ID获取Bean，但更多的时候我们根据Bean的类型获取Bean的引用：
        UserService userService = context.getBean(UserService.class);

        //登录:
        User user = userService.login("bob@example.com", "password");
        System.out.println("----"+user.toString());


        /**
         * Spring还提供另一种IoC容器叫BeanFactory，使用方式和ApplicationContext类似：
         * BeanFactory的实现是按需创建，即第一次获取Bean时才创建这个Bean，
         *
         * 很少会考虑使用BeanFactory。
         * 因为按需创建的时候，发现依赖有问题再报个错，还不如启动就报错
         */
        // BeanFactory factory = new XmlBeanFactory(new ClassPathResource("application.xml"));
        // MailService mailService = factory.getBean(MailService.class);
    }
}
