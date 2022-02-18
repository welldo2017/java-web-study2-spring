package com.welldo.spring2;

import com.welldo.spring2.bean.User;
import com.welldo.spring2.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 0.使用Spring的IoC容器，
 * 实际上就是通过类似XML这样的配置文件，把我们自己的Bean的依赖关系描述出来，然后让容器来创建并装配Bean。
 * (装配，其实就是调用 set属性的方法)
 * 一旦容器初始化完毕，我们就直接从容器中获取Bean使用它们。
 *
 * 1. 使用XML配置的优点是所有的Bean都能一目了然地列出来，并通过配置注入能直观地看到每个Bean的依赖。
 * 它的缺点是写起来非常繁琐，每增加一个组件，就必须把新的Bean配置到XML中。
 *
 * 2. 我们可以使用Annotation配置，可以完全不需要XML，让Spring自动扫描Bean并组装它们。
 *
 * author:welldo
 * date: 2022-02-18 13:19
 */


@ComponentScan  //自动搜索当前类所在的包以及子包，把所有标注为@Component的Bean自动创建出来，并根据@Autowired进行装配。
@Configuration  //表示它是一个配置类,通常来说，配置类放在顶层包。
public class Main {

    static ApplicationContext context = null;

    public static void main(String[] args) throws InterruptedException {

        //5. 本类的类名上加了一个 @Configuration,表示它是一个配置类,通常来说，配置类放在顶层包。
        //因为我们创建ApplicationContext时：
        //使用的实现类是 AnnotationConfigApplicationContext，此类的构造器，必须传入一个标注了@Configuration的类名。
        context = new AnnotationConfigApplicationContext(Main.class);
        System.out.println(context);

        UserService userService = context.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());


        /*
         * 6.总结：
         * 使用Annotation配合自动扫描能, 大幅简化Spring的配置，我们只需要保证：
         * 每个Bean被标注为 @Component并正确使用@Autowired注入；
         * 配置类被标注为@Configuration和@ComponentScan；
         * 所有Bean均在指定包以及子包内。
         */
        for ( ; ;) {
            Thread.sleep(1000);
        }
    }
}
