package com.welldo.spring6;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 0.
 * 与声明式事务相对应的,是编程式事务，手动去实现 事务的开启与回滚等操作，这些逻辑耦合在代码中。
 * 声明式事务就是为了不写 事务开启/提交/回滚的代码
 *
 * author:welldo
 * date: 2022-02-21 14:15
 */

// @EnableAspectJAutoProxy     //声明了@EnableTransactionManagement后，不必额外添加@EnableAspectJAutoProxy。
@EnableTransactionManagement // 启用声明式 事务管理
@PropertySource("jdbc.properties")
@ComponentScan
@Configuration
public class AppConfig {

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);

        // 由于加了事务,spring使用AOP代理，即通过自动创建Bean的Proxy实现：
        //UserService$$EnhancerBySpringCGLIB$$12dff485 ,
        System.out.println(userService.getClass());

        // 查找 Bob:
        if (userService.fetchUserByEmail("bob@example.com") == null) {
            userService.register("bob@example.com", "password1", "Bob");
        }
        // 查找 Alice:
        if (userService.fetchUserByEmail("alice@example.com") == null) {
            userService.register("alice@example.com", "password2", "Alice");
        }
        // 查找 Tom:
        if (userService.fetchUserByEmail("tom@example.com") == null) {
            userService.register("tom@example.com", "password2", "Tom");
        }

        // 插入Root: 会抛异常,触发事务回滚,不能插入成功
        try {
            userService.register("root@example.com", "password3", "root");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

        // 查询所有用户:(没有root用户)
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }

        //关闭
        ((AnnotationConfigApplicationContext) context).close();
    }


    //创建一个数据库连接池
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


    //创建一个事务管理器
    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //创建一个 jdbcTemplate
    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
