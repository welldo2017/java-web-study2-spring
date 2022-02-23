package com.welldo.spring8;

import com.welldo.spring7.dao.UserDao;
import com.welldo.spring8.bean.User;
import com.welldo.spring8.service.UserService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 集成Hibernate
 *
 * 1.使用JdbcTemplate的时候，我们用得最多的方法就是List<T> query(String sql, Object[] args, RowMapper rowMapper)。
 * 比如 {@link UserDao}
 * 这个RowMapper的作用就是把ResultSet的一行记录映射为Java Bean。
 *
 * 这种把关系数据库的表记录映射为Java对象的过程就是ORM：Object-Relational Mapping
 * 使用JdbcTemplate配合RowMapper可以看作是最原始的ORM。
 * 如果要实现更自动化的ORM，可以选择成熟的ORM框架，例如Hibernate。 * 它是第一个被广泛使用的ORM框架
 *
 * 2.在Spring中集成Hibernate需要配置的Bean如下：
 *      DataSource；
 *      LocalSessionFactory；
 *      HibernateTransactionManager；
 *      HibernateTemplate（推荐）。
 *
 * author:welldo
 * date: 2022-02-22 14:15
 */
@PropertySource("jdbc.properties")
@EnableTransactionManagement
@ComponentScan
@Configuration
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        if (userService.fetchUserByEmail("bob@example.com") == null) {
            User bob = userService.register("bob@example.com", "bob123", "Bob");
            System.out.println("Registered ok: " + bob);
        }
        if (userService.fetchUserByEmail("alice@example.com") == null) {
            User alice = userService.register("alice@example.com", "helloalice", "Bob");
            System.out.println("Registered ok: " + alice);
        }
        // 查询所有用户:
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }
        User bob = userService.login("bob@example.com", "bob123");
        System.out.println(bob);
        ((ConfigurableApplicationContext) context).close();
    }


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
     * 1.
     * (Spring也提供了工厂模式，允许定义一个工厂，然后由工厂创建真正的Bean。)
     * LocalSessionFactoryBean是Spring提供的一个工厂,它会自动创建一个 SessionFactory
     * SessionFactory是Hibernate提供的最核心的一个对象
     *
     * 1.Session 封装了 jdbc的connectin(连接)
     * SessionFactory 封装了 jdbc的DataSoure(连接池)
     */
    @Bean
    LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
        Properties props = new Properties();

        //在数据库中,自动创建的表结构 ,生产环境不要使用
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        //指示Hibernate,使用的数据库是HSQLDB (“翻译”成SQL时，会根据设定的“方言”,来生成有针对性的优化的SQL)
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        // 让Hibernate打印执行的SQL，对调试非常有用
        props.setProperty("hibernate.show_sql", "true");

        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setHibernateProperties(props);
        sessionFactoryBean.setDataSource(dataSource);
        // 扫描指定的package获取所有entity class,自动找出能映射为数据库表记录的JavaBean
        sessionFactoryBean.setPackagesToScan("com.welldo.spring8");

        return sessionFactoryBean;
    }


    //2.HibernateTemplate是Spring为了便于我们使用Hibernate提供的工具类
    @Bean
    HibernateTemplate createHibernateTemplate(@Autowired SessionFactory sessionFactory) {
        return new HibernateTemplate(sessionFactory);
    }

    //2. HibernateTransactionManager是配合Hibernate使用声明式事务所必须的
    @Bean
    PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
