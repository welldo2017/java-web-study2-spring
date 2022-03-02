package com.welldo.spring.spring5db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

/**
 * 4.现在，所有准备工作都已完毕。我们只用在需要访问数据库的Bean中，注入JdbcTemplate即可：
 *
 * author:welldo
 * date: 2022-02-21 14:31
 */
@Component
public class UserService {


    //需要强调的是，JdbcTemplate只是对JDBC操作的一个简单封装，它的目的是尽量减少手动编写try(resource) {...}的代码，
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 1. T execute(ConnectionCallback<T> action)方法，它提供了Jdbc的Connection供我们使用：
     */
    public User getUserById(long id){
        String sql = "select * from users where id = ?";

        // 注意:传入的是ConnectionCallback:
        User user = jdbcTemplate.execute((Connection conn) -> {

            // 可以直接使用conn 实例，不要释放它，回调结束后,由JdbcTemplate释放它:
            // 在内部手动创建的PreparedStatement、ResultSet必须用try(...)释放:
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, id);//索引从1开始

                try (ResultSet rs = ps.executeQuery()) {
                    //查到了
                    if (rs.next()) {
                        return new User( // new User object:
                                rs.getLong("id"), // id
                                rs.getString("email"), // email
                                rs.getString("password"), // password
                                rs.getString("name")); // name
                    }

                    //没查到
                    throw new RuntimeException("user not found by id.");
                }
            }
        });

        return user;
    }

    /**
     * 2.  T execute(String sql, PreparedStatementCallback<T> action)
     * 需要传入SQL语句，以及PreparedStatementCallback:
     */
    public User getUserByName(String name){
        String sql = "select * from users where name = ?";

        //conn, PreparedStatement实例已经由JdbcTemplate创建，并在回调后自动释放:
        // 在内部手动创建的 ResultSet 必须用try(...)释放:
        User user = jdbcTemplate.execute(sql, (PreparedStatement ps) -> {
            ps.setObject(1, name);//索引从1开始

            try (ResultSet rs = ps.executeQuery()) {
                //有结果
                if (rs.next()) {
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                }
                // 没有结果
                throw new RuntimeException("user not found by id.");
            }
        });
        return user;
    }


    /**
     * 3. T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper)
     *
     * 在queryForObject()方法中，传入SQL以及SQL参数后，
     * JdbcTemplate会自动创建 conn, PreparedStatement,ResultSet,并执行查询
     * 整个过程中，使用Connection、PreparedStatement和ResultSet都不需要我们手动管理。
     *
     * 我们提供的RowMapper需要做的事情就是把 ResultSet 的当前行映射成一个JavaBean并返回
     */
    public User getUserByEmail(String email) {
        String sql = "select * from users where email = ?";

        // 依次传入SQL，参数和RowMapper实例:
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    // 将ResultSet的当前行映射为一个JavaBean:
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
    }

    //3.5
    //RowMapper不一定返回JavaBean，实际上它可以返回任何Java对象。例如，使用SELECT COUNT(*)查询时，可以返回Long：
    public long getUsers() {
        String sql = "SELECT COUNT(*) FROM users";

        return jdbcTemplate.queryForObject(
                sql,null,
                (ResultSet rs, int rowNum) -> {
            return rs.getLong(1);   // SELECT COUNT(*)查询只有一列，取第一列数据:
        });
    }


    /**
     * 4.
     * 如果我们期望返回多行记录，而不是一行，可以用query()方法：
     * 和queryForObject()一样, query()方法传入的参数仍然是SQL、SQL参数以及 RowMapper 实例。
     *
     * 这里我们直接使用Spring提供的 BeanPropertyRowMapper。
     * 如果数据库表的结构恰好和JavaBean的属性名称一致，那么BeanPropertyRowMapper就可以直接把一行记录按列名转换为JavaBean。
     *
     * 如果表结构和JavaBean不一致怎么办？那就需要稍微改写一下查询，使结果集的结构和JavaBean保持一致。
     * 例如，表的列名是office_address，而JavaBean属性是workAddress，就需要指定别名，改写查询如下：
     * SELECT id, email, office_address AS workAddress, name FROM users WHERE email = ?
     */
    public List<User> getUsers(int pageIndex) {
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        return jdbcTemplate.query(
                "SELECT * FROM users LIMIT ? OFFSET ?",
                new Object[] { limit, offset },
                new BeanPropertyRowMapper<>(User.class));
    }



    //5.如果我们执行的不是查询，而是插入、更新和删除操作，那么需要使用update()方法：
    public void updateUser(User user) {
        // 传入SQL，SQL参数，返回更新的行数:
        if (1 != jdbcTemplate.update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by id");
        }
    }

    /**
     * 5.1
     * 只有一种INSERT操作比较特殊，那就是如果某一列是自增列（例如自增主键），
     * 通常，我们需要获取插入后的自增值。
     * JdbcTemplate提供了一个KeyHolder来简化这一操作：
     */
    public User register(String email, String password, String name) {
        // 创建一个KeyHolder:
        KeyHolder holder = new GeneratedKeyHolder();

        if (1 != jdbcTemplate.update(
                // 参数1:PreparedStatementCreator
                (conn) -> {
                    // 创建PreparedStatement时，必须指定 RETURN_GENERATED_KEYS:
                    PreparedStatement  ps = conn.prepareStatement(
                            "INSERT INTO users(email,password,name) VALUES(?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, email);
                    ps.setObject(2, password);
                    ps.setObject(3, name);
                    return ps;
                },
                // 参数2:KeyHolder
                holder)
                ) {
            throw new RuntimeException("Insert failed.");
        }
        // 从KeyHolder中获取返回的自增值:
        return new User(holder.getKey().longValue(), email, password, name);
    }

}
