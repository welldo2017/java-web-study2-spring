package com.welldo.spring4aop.a3;

import com.welldo.spring4aop.a3.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
/**
 * 1. AOP原理
 * 如何把切面织入到核心逻辑中？这正是AOP需要解决的问题。
 * 换句话说，如果客户端获得了BookService的引用，
 * 当调用bookService.createBook()时，如何对调用方法进行拦截，并在拦截前后进行安全检查、日志、事务等处理，就相当于完成了所有业务功能。
 *
 * 在Java平台上，对于AOP的织入，有3种方式：
 *
 * 编译期：在编译时，由编译器把切面调用编译进字节码，这种方式需要定义新的关键字并扩展编译器，AspectJ就扩展了Java编译器，使用关键字aspect来实现织入；
 * 类加载器：在目标类被装载到JVM时，通过一个特殊的类加载器，对目标类的字节码重新“增强”；
 * 运行期：目标对象和切面都是普通Java类，通过JVM的动态代理功能或者第三方库实现运行期动态织入。
 *
 * 最简单的方式是第三种，Spring的AOP实现就是基于JVM的动态代理
 *
 * AOP技术看上去比较神秘，但实际上，它本质就是一个动态代理，
 *
 * 2.装配aop
 *
 * 在AOP编程中，我们经常会遇到下面的概念：
 *  Aspect：切面，即一个横跨多个核心逻辑的功能，或者称之为系统关注点；
 *  Joinpoint：连接点，即定义在应用程序流程的何处插入切面的执行；
 *  Pointcut：切入点，即一组连接点的集合；
 *  Advice：增强，指特定连接点上执行的动作；
 *  Introduction：引介，指为一个已有的Java对象动态地增加新的接口；
 *  Weaving：织入，指将切面整合到程序的执行流程中；
 *  Interceptor：拦截器，是一种实现增强的方式；
 *  Target Object：目标对象，即真正执行业务的核心逻辑对象；
 *  AOP Proxy：AOP代理，是客户端持有的增强后的对象引用。
 *
 * AOP本质上只是一种代理模式的实现方式，在Spring的容器中实现AOP特别方便。
 *
 * 我们通过Maven引入Spring对AOP的支持：
 * <dependency>
 *     <groupId>org.springframework</groupId>
 *     <artifactId>spring-aspects</artifactId>
 *     <version>${spring.version}</version>
 * </dependency>
 * 上述依赖会自动引入AspectJ，使用AspectJ实现AOP比较方便，因为它的定义比较简单。
 *
 * 然后，我们定义一个LoggingAspect： * {@link LoggingAspect}
 *
 *
 * author:welldo
 * date: 2022-02-20 17:09
 */

@Configuration
@ComponentScan
@EnableAspectJAutoProxy //ioc会查找带有@Aspect的Bean，然后根据每个方法的@Before、@Around等注解把AOP注入到特定的Bean中
public class AppConfig {

    public static void main(String[] args)  {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        userService.register("test@example.com", "password", "test");
        userService.login("bob@example.com", "password");

        //原理:
        // 最简单的方法是编写一个子类，并持有原始实例的引用：
        // public UserServiceAopProxy extends UserService

        //Spring容器启动时,为我们自动创建了代理类，它取代了原始的UserService,并注入Aspect.
        //原始的UserService实例作为内部变量隐藏在UserServiceAopProxy中）。
        // 如果我们打印从Spring容器获取的UserService实例类型，
        // 它类似: UserService$$EnhancerBySpringCGLIB$$1f44e01c
        System.out.println(userService.getClass().getName());

    }

}
