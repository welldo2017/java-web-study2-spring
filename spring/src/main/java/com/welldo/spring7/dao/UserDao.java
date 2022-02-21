package com.welldo.spring7.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.welldo.spring7.service.User;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 继承链 UserDao extends AbstractDao extends JdbcDaoSupport
 */
@Component
@Transactional
public class UserDao extends AbstractDao<User> {
    // 从父类获得了:
    // User getById(long)
    // List<User> getAll(int)
    // void deleteById(long)

    public User fetchUserByEmail(String email) {
        List<User> users = getJdbcTemplate().query("SELECT * FROM users WHERE email = ?", new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
        return users.isEmpty() ? null : users.get(0);
    }

    public User getUserByEmail(String email) {
        return getJdbcTemplate().queryForObject("SELECT * FROM users WHERE email = ?", new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
    }

    public User login(String email, String password) {
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("login failed.");
    }

    //不能插入root
    public User createUser(String email, String password, String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != getJdbcTemplate().update((conn) -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, email);
            ps.setObject(2, password);
            ps.setObject(3, name);
            return ps;
        }, holder)) {
            throw new RuntimeException("Insert failed.");
        }
        if ("root".equalsIgnoreCase(name)) {
            throw new RuntimeException("Invalid name, will rollback...");
        }
        return new User(holder.getKey().longValue(), email, password, name);
    }



    public void updateUser(User user) {
        if (1 != getJdbcTemplate().update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by id");
        }
    }
}