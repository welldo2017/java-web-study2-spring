package com.welldo.spring.spring6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * author:welldo
 * date: 2022-02-09 18:06
 */
@Component
public class DbInitializer {

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
        System.out.println("清除临时表...");
        jdbcTemplate.update("DROP TABLE users");
    }
}
