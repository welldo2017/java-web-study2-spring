package com.welldo.spring.spring7;


import com.welldo.spring.spring7.service.User;
import com.welldo.spring.spring7.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 使用DAO
 * DAO模式就是一个简单的数据访问模式，是否使用DAO，根据实际情况决定，
 * 因为很多时候，直接在Service层操作数据库也是完全没有问题的。
 *
 * author:welldo
 * date: 2022-02-21 14:15
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);

        // 插入Bob:
        if (userService.fetchUserByEmail("bob@example.com") == null) {
            userService.register("bob@example.com", "password1", "Bob");
        }
        // 插入Alice:
        if (userService.fetchUserByEmail("alice@example.com") == null) {
            userService.register("alice@example.com", "password2", "Alice");
        }
        // 插入Tom:
        if (userService.fetchUserByEmail("tom@example.com") == null) {
            userService.register("tom@example.com", "password2", "Tom");
        }
        // 插入Root:
        try {
            userService.register("root@example.com", "password3", "root");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        // 查询所有用户:
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }
        ((ConfigurableApplicationContext) context).close();
    }

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;

    @Bean
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.setJdbcUrl(jdbcUrl);

        //由于需要手动管理事务了,所以不能自动提交
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
    TransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
