package com.welldo.spring.spring5db;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * JDBC代码的关键是使用try ... finally释放资源，涉及到事务的代码需要正确提交或回滚事务。
 *
 * author:welldo
 * date: 2022-02-21 14:15
 */
@PropertySource("jdbc.properties")  //0. 通过@PropertySource("jdbc.properties")读取数据库配置文件
@ComponentScan
@Configuration
public class AppConfig {

    @Value("${jdbc.url}")   //0.注入配置文件的相关配置
    String jdbcUrl;

    //username的值默认=sa
    @Value("${jdbc.username}")
    String jdbcUsername;

    //默认=空字符串
    @Value("${jdbc.password}")
    String jdbcPassword;


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);

        //注册
        userService.register("bob@example.com", "password1", "Bob");
        userService.register("alice@example.com", "password2", "Alice");

        User bob = userService.getUserByName("Bob");
        System.out.println(bob);

        User tom = userService.register("tom@example.com", "password3", "Tom");
        System.out.println(tom);

        System.out.println("Total: " + userService.getUsers());
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }

        //销毁,调用 DatabaseInitializer的 clean() 方法
        ((ConfigurableApplicationContext) context).close();
    }



    /**
     * 1.通过IoC容器, 创建全局DataSource实例，表示数据库连接池；
     * 注意,这里是 javax.sql.DataSource
     *
     * DataSource实例，它的实际类型是 HikariDataSource ，创建时需要用到注入的配置；
     */
    @Bean
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);

        config.addDataSourceProperty("autoCommit", "true");//自动提交
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }


    /**
     * 2. Spring提供了一个JdbcTemplate，可以方便地让我们操作JDBC，
     * 因此，通常情况下，我们会实例化一个JdbcTemplate ,它需要注入DataSource(这里通过方法参数完成注入的)
     */
    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
