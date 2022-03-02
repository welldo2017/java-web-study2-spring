package com.welldo.spring.spring3bprop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

/**
 * 要读取配置文件，我们可以使用上一节 {@link com.welldo.spring.spring3atxt.Main}
 * 讲到的Resource来读取位于classpath下的一个app.properties文件。
 * 但是，这样仍然比较繁琐。因为它是以文本形式读进来的,不能自动转成kv形式
 *
 * Spring容器还提供了一个更简单的@PropertySource来自动读取配置文件。
 * 我们只需要在@Configuration配置类上再添加一个注解： * @PropertySource("app.properties")
 * 表示读取classpath的app.properties
 * Spring容器看到@PropertySource("app.properties")注解后，自动读取这个配置文件，然后，我们使用@Value正常注入：
 *
 * 极大地简化读取配置的麻烦。
 *
 * 2.另一种注入配置的方式是先通过一个简单的JavaBean持有所有的配置，例如，一个 SmtpConfig：
 *
 * author:welldo
 * date: 2022-02-20 15:00
 */

//亲测:这个属性,可以写在configuration上;也可以写在任意一个 component上.
//也就是说,@PropertySource读取到的配置,整个IoC容器都有效
// 最好写在 configuration上.免得太乱.
@PropertySource("app.properties")
@ComponentScan
@Configuration
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        //1.1 获取自定义的类的bean
        Prop prop = context.getBean(Prop.class);
        System.out.println(prop.appName);

        //1.2 获取第三方类的bean
        ZoneId bean = context.getBean(ZoneId.class);
        System.out.println(bean);

        //2.从专门的javabean中获取.
        MailService mailService = context.getBean(MailService.class);
        System.out.println(mailService.smtpHost);
        System.out.println(mailService.smtpPort);
    }

    //2.
    //"${app.zone}"  表示读取key为app.zone的value，如果key不存在，启动将报错；
    //"${app.zone:Z}"表示读取key为app.zone的value，但如果key不存在，就使用默认值Z。
    @Value("${app.zone:UTC+08:00}")
    String zoneId;

    @Bean
    ZoneId createZoneId() {
        return ZoneId.of(zoneId);
    }

}
