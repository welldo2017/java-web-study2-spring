package com.welldo.web.web1;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * 0. 注意，这个工程的打包方式是war包。
 *
 *
 * 1.在Web开发一章中，我们已经详细介绍了JavaEE中Web开发的基础：Servlet。
 *      a）Servlet规范定义了几种标准组件：Servlet、Filter和Listener；
 *          ·Servlet：能处理HTTP请求并将HTTP响应返回；
 *          ·JSP：一种嵌套Java代码的HTML，将被编译为Servlet；
 *          ·Filter：能过滤指定的URL以实现拦截功能；
 *          ·Listener：监听指定的事件，如ServletContext、HttpSession的创建和销毁。
 *
 *      b）Servlet的标准组件总是运行在Servlet容器中，如Tomcat
 *
 *      c）此外，Servlet容器为每个Web应用程序自动创建一个唯一的 ServletContext 实例，这个实例就代表了Web应用程序本身。
 *
 * 2.直接使用Servlet进行Web开发，好比直接在JDBC上操作数据库，比较繁琐，
 * 更好的方法是在Servlet基础上封装MVC框架，基于MVC开发Web应用，大部分时候，不需要接触Servlet API，开发省时省力。
 *
 * 3.创建maven工程，引入依赖请查看pom
 *
 * 4.src/main/webapp是标准web目录，
 *      WEB-INF存放web.xml，编译的class，第三方jar，以及不允许浏览器直接访问的View模版
 *      static目录存放所有静态文件。
 *
 * 5.在src/main/resources目录中,存放的是Java程序读取的classpath资源文件
 *
 * 6.除了创建DataSource、JdbcTemplate、PlatformTransactionManager外，AppConfig需要额外创建几个用于Spring MVC的Bean：
 *  WebMvcConfigurer
 *  ViewResolver
 *
 *
 * 7.剩下的Bean都是普通的@Component，但Controller必须标记为@Controller
 * {@link com.welldo.web.web1.controller.UserController}
 *
 *
 * 8. * 如果是普通的Java应用程序，我们通过main()方法可以很简单地创建一个Spring容器的实例：
 * public static void main(String[] args) {
 *     ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
 * }
 *
 * 但是，现在是Web应用程序，而Web应用程序总是由Servlet容器（如tomcat）创建，
 * 那么，Spring容器应该由谁创建？在什么时候创建？
 * 请查看 web.xml
 *
 *
 * 9.小结
 * 使用Spring MVC时，整个Web应用程序按如下顺序启动：
 *
 * 启动Tomcat服务器；
 * Tomcat读取web.xml并初始化DispatcherServlet；
 * DispatcherServlet创建IoC容器并自动注册到ServletContext中。
 * 启动后，浏览器发出的HTTP请求全部由DispatcherServlet接收，并根据配置转发到指定Controller的指定方法处理。
 *
 *
 *
 *
 * author:welldo
 * date: 2022-03-03 14:15
 */
@Configuration
@ComponentScan
@EnableWebMvc // 启用Spring MVC
@EnableTransactionManagement
@PropertySource("classpath:/jdbc.properties")
public class AppConfig {

    // -- jdbc configuration --------------------------------------------------
    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;




    /**
     * 6.1
     * WebMvcConfigurer并不是必须的，
     * 但我们在这里创建一个默认的WebMvcConfigurer，只覆写addResourceHandlers()，
     * 目的是让Spring MVC自动处理静态文件，并且映射路径为/static/**。
     */
    @Bean
    WebMvcConfigurer createWebMvcConfigurer() {
        //匿名内部类
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }
        };
    }


    // -- pebble view configuration -------------------------------------------
    /**
     * 6.2
     * Spring MVC允许集成任何模板引擎，使用哪个模板引擎，就实例化一个对应的ViewResolver：
     * 指定模板文件存放在/WEB-INF/templates/目录下。
     *
     * 注意：PebbleEngine版本是3.1.2，不能用3.1.5
     */
    @Bean
    ViewResolver createViewResolver(@Autowired ServletContext servletContext) {
        PebbleEngine engine = new PebbleEngine.Builder()
                .autoEscaping(true)// 默认打开HTML字符转义，防止XSS攻击
                .cacheActive(false)// 禁用缓存使得每次修改模板可以立刻看到效果
                .loader(new ServletLoader(servletContext))
                .extension(new SpringExtension())
                .build();
        PebbleViewResolver viewResolver = new PebbleViewResolver();
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix("");
        viewResolver.setPebbleEngine(engine);
        return viewResolver;
    }



    @Bean
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "false");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
