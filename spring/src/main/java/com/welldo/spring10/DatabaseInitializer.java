package com.welldo.spring10;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {

        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
                        + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "UNIQUE (email))");
            }
        }
    }

    @PreDestroy
    public void clean() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("清除临时表...");
                stmt.execute("DROP TABLE users");
            }
        }
    }
}