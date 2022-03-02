package com.welldo.spring.spring10;

import com.welldo.spring.spring10.bean.User;
import com.welldo.spring.spring10.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 集成mybatis
 * (官方文档  https://mybatis.org/mybatis-3/zh/configuration.html )
 *
 * 0.使用Hibernate或JPA操作数据库时，
 * 这类ORM干的主要工作就是把ResultSet的每一行变成Java Bean，或者把Java Bean自动转换到INSERT或UPDATE语句的参数中，
 * 从而实现ORM。
 *
 * 1.Spring提供的JdbcTemplate，它和ORM框架相比，主要有几点差别：
 * 查询后需要手动提供Mapper实例以便把ResultSet的每一行变为Java对象；
 * 增删改操作所需的参数列表，需要手动传入，即把User实例变为[user.id, user.name, user.email]这样的列表，比较麻烦。
 *
 * 但是JdbcTemplate的优势在于它的确定性：即每次读取操作一定是数据库操作而不是缓存，所执行的SQL是完全确定的，
 * 缺点就是代码比较繁琐，构造INSERT INTO users VALUES (?,?,?)更是复杂。
 *
 * 2.所以，介于全自动ORM如Hibernate和手写全部如JdbcTemplate之间，还有一种半自动的ORM，
 * 它只负责把ResultSet自动映射到Java Bean，或者自动填充Java Bean参数，但仍需自己写出SQL。
 * MyBatis就是这样一种半自动化ORM框架。
 *
 *
 * 3.如何在Spring中集成MyBatis。
 *      org.mybatis:mybatis:3.5.4
 *      org.mybatis:mybatis-spring:2.0.4
 *
 *
 * 4.核心概念的横向对比
 * -----------------------------------------------------
 * JDBC	        Hibernate	    	MyBatis             框架
 * -----------------------------------------------------
 * DataSource	SessionFactory		SqlSessionFactory   数据库连接池
 * Connection	Session	        	SqlSession          数据库连接
 * -----------------------------------------------------
 *
 * author:welldo
 * date: 2022-02-23 14:15
 */
@PropertySource("jdbc.properties")
@EnableTransactionManagement
@ComponentScan
@Configuration
@MapperScan("com.welldo.spring.spring10.mapper")
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        if (userService.fetchUserByEmail("bob@example.com") == null) {
            User bob = userService.register("bob@example.com", "bob123", "Bob");
            System.out.println("Registered ok: " + bob);
        }
        if (userService.fetchUserByEmail("alice@example.com") == null) {
            User alice = userService.register("alice@example.com", "helloalice", "Alice");
            System.out.println("Registered ok: " + alice);
        }
        if (userService.fetchUserByEmail("tom@example.com") == null) {
            User tom = userService.register("tom@example.com", "tomcat", "Alice");
            System.out.println("Registered ok: " + tom);
        }
        // 查询所有用户:
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }
        System.out.println("login...");
        User tom = userService.login("tom@example.com", "tomcat");
        System.out.println(tom);



        ((ConfigurableApplicationContext) context).close();
    }

    //1.dataSource
    @Bean
    DataSource createDataSource(
        @Value("${jdbc.url}") String jdbcUrl,
        @Value("${jdbc.username}") String jdbcUsername,
        @Value("${jdbc.password}") String jdbcPassword) {

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(jdbcUsername);
            config.setPassword(jdbcPassword);

            config.addDataSourceProperty("autoCommit", "false");
            config.addDataSourceProperty("connectionTimeout", "5");
            config.addDataSourceProperty("idleTimeout", "60");
            return new HikariDataSource(config);
    }


    /**
     * 2.mybatis的核心
     *
     * 疑问:
     * SqlSessionFactoryBean 没有显式地用在哪里，为什么?
     * 答案:
     * 注意: @MapperScan("com.itranswarp.learnjava.mapper")
     * MyBatis在启动时自动给每个Mapper接口创建如下Bean：
     *
     * @Component
     * public class UserMapperImpl implements UserMapper {
     *     @Autowired
     *     SqlSessionFactory sessionFactory;
     *
     *     public List<User> getAllUsers() {
     *         String sql = getSqlFromAnnotation(...);
     *         try (SqlSession session = sessionFactory.createSession()) {
     *             ...
     *         }
     *     }
     * }
     * Spring允许动态创建Bean并添加到applicationContext中
     */
    @Bean
    SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    //3.MyBatis可以直接使用Spring管理的声明式事务，因此，创建事务管理器和使用JDBC是一样的：
    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}
