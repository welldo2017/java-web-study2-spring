package com.welldo.spring.spring3;

import com.welldo.spring.spring3.bean.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

/**
 * author:welldo
 * date: 2022-02-18 13:19
 */
@ComponentScan
@Configuration
public class Main {
    static ApplicationContext context = null;

    public static void main(String[] args)  {
        context = new AnnotationConfigApplicationContext(Main.class);

        // test1();//测试单例

        // test2();//注入List

        // test3();//可选注入

        // test4((AnnotationConfigApplicationContext)context);//初始化与销毁
    }

    //1.把bean 设置为singleton（单例），或者Prototype（原型）
    //没事别用Prototype
    private static void test1(){
        System.out.println(context.getBean(A1_MailSession.class));
        System.out.println(context.getBean(A1_MailSession.class));
        System.out.println(context.getBean(A1_MailSession.class));
        System.out.println(context.getBean(A1_MailSession.class));
    }



    //2. 注入List
    private static void test2(){
        A2_ValidatorEntry validator = context.getBean(A2_ValidatorEntry.class);
        String email ="welldo2012qq.com";//没有@，email无效
        String name ="welldo";
        String pwd ="welldo";
        validator.validate(email,name,pwd);
    }


    /**
     * 3.可选注入
     * 这个方法,返回值,取决于 createPerson() 方法和 A3_Optional类
     * 如果有 createPerson（）方法，则返回方法中定义的
     * 如果没有，则返回 默认创建的
     */
    public static void test3(){
        A3_Optional bean = context.getBean(A3_Optional.class);
        System.out.println(bean.person.getName());
    }

    //注意:这个方法一定要加上 @bean
    @Bean
    Person createPerson(){
        return new Person("Main方法中创建的person");
    }



    /**
     * 创建第三方Bean
     * 如果一个Bean不在我们自己的package管理之内，例如ZoneId，如何创建它？
     * 答案是我们自己在@Configuration类中编写一个Java方法,创建并返回它，注意给方法标记一个@Bean注解：
     *
     * Spring对标记为@Bean的方法只调用一次，因此返回的Bean仍然是单例。
     *
     * 这个方法不能private
     * BeanDefinitionParsingException: Configuration problem: @Bean method 'createZoneId' must not be private or final;
     *
     */
    // @Bean("Chicago")
    // @Primary
    //  ZoneId createZoneId() {
    //     return ZoneId.of("America/Chicago");
    // }



    /**
     * 4.初始化与销毁
     * ApplicationContext 没有close方法,只有它的子类才有
     */
    public static void test4(AnnotationConfigApplicationContext applicationContext){
        A4_InitAndDestroy a4 = context.getBean(A4_InitAndDestroy.class);
        System.out.println(a4.person);

        applicationContext.close();
    }


    /**
     * 5.使用别名
     *
     * 默认情况下，对一种类型的Bean，容器只创建一个实例。
     * 但有些时候，我们需要对一种类型的Bean创建多个实例。例如，同时连接多个数据库，就必须创建多个DataSource实例。
     *
     * a.这个时候,必须指定别名(@Bean("name")指定别名),以免报错 NoUniqueBeanDefinitionException
     * b1.注入时，要指定Bean的名称 ,以免报错 expected single matching bean but found 2+
     * b2.还有一种方法是把其中某个Bean指定为@Primary：
     *
     * 这个方法不能private
     * BeanDefinitionParsingException: Configuration problem: @Bean method 'createZoneId' must not be private or final;
     */
    @Bean("z")
    ZoneId createZoneOfZ() {
        return ZoneId.of("Z");
    }

    @Bean("utc8")
    ZoneId createZoneOfUTC8() {
        return ZoneId.of("UTC+08:00");
    }
}
