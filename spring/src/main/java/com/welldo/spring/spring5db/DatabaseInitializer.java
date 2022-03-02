package com.welldo.spring.spring5db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 3. 通过HSQLDB自带的工具来初始化数据库表，这里我们写一个Bean，在Spring容器启动时自动创建一个users表：
 * author:welldo
 * date: 2022-02-21 14:24
 */

@Component
public class DatabaseInitializer {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * PostConstruct注释用在方法上，该方法需要在依赖项注入完成后执行，以执行任何初始化。
     * 必须在类投入使用之前调用此方法。
     */
    @PostConstruct
    public void init() {
        //如果表存在,先删除
        jdbcTemplate.update("DROP TABLE IF EXISTS users");

        /*创建 users表,
        id(自增),email(唯一键),password,name
         */
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS users (" //
                + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                + "email VARCHAR(100) NOT NULL, " //
                + "password VARCHAR(100) NOT NULL, " //
                + "name VARCHAR(100) NOT NULL, " //
                + "UNIQUE (email))");
    }

    @PreDestroy
    public void clean() {
        jdbcTemplate.update("DROP TABLE users");
    }
}
