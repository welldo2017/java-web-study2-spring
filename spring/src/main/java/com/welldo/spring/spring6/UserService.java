package com.welldo.spring.spring6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * author:welldo
 * date: 2022-02-21 14:31
 */

@Transactional      //表示所有的public方法,都会加上事务.
@Component
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User fetchUserByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE email = ?",
                new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
        return users.isEmpty() ? null : users.get(0);
    }



    public List<User> getUsers(int pageIndex) {
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?", new Object[] { limit, offset },
                new BeanPropertyRowMapper<>(User.class));
    }



    //插入
    public User register(String email, String password, String name) {
        KeyHolder holder = new GeneratedKeyHolder();

        if (1 != jdbcTemplate.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users(email, password, name) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, email);
            ps.setObject(2, password);
            ps.setObject(3, name);
            return ps;
        }, holder)) {
            throw new RuntimeException("Insert failed.");
        }

        //不能使用root 这个用户名
        if ("root".equalsIgnoreCase(name)) {
            throw new RuntimeException("Invalid name, will rollback...");
        }
        return new User(holder.getKey().longValue(), email, password, name);
    }

}
