package com.welldo.spring.spring7.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 编写一个AbstractDao，专门负责注入JdbcTemplate：
 * 为何要注入呢?
 *
 * 因为Spring提供了一个JdbcDaoSupport类，用于简化DAO的实现。
 * 这个JdbcDaoSupport没什么复杂的，核心代码就是持有一个JdbcTemplate：
 *
 * public abstract class JdbcDaoSupport extends DaoSupport {
 *
 *  //因为JdbcDaoSupport的jdbcTemplate字段没有标记@Autowired，
 *  //所以，子类想要注入JdbcTemplate，还得自己想个办法：
 *     private JdbcTemplate jdbcTemplate;
 *
 *     public final void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
 *         this.jdbcTemplate = jdbcTemplate;
 *         initTemplateConfig();
 *     }
 *
 *     public final JdbcTemplate getJdbcTemplate() {
 *         return this.jdbcTemplate;
 *     }
 *
 *     ...
 * }
 *
 * 所以,这个 AbstractDao 专门负责注入JdbcTemplate
 *
 * 继承链 AbstractDao extends JdbcDaoSupport
 *
 *
 * author:welldo
 * date: 2022-02-21 14:15
 */
public abstract class AbstractDao<T> extends JdbcDaoSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //用于给 JdbcDaoSupport 注入 JdbcTemplate
    @PostConstruct
    public void init() {
        super.setJdbcTemplate(jdbcTemplate);
    }

    private String table;
    private Class<T> entityClass;
    private RowMapper<T> rowMapper;


    //构造器
    public AbstractDao() {
        this.entityClass = getParameterizedType();
        this.table = this.entityClass.getSimpleName().toLowerCase() + "s";
        this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
    }


    //给子类用的方法
    public T getById(long id) {
        return getJdbcTemplate().queryForObject("SELECT * FROM " + table + " WHERE id = ?", this.rowMapper, id);
    }

    //给子类用的方法
    public List<T> getAll(int pageIndex) {
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        return getJdbcTemplate().query(
                "SELECT * FROM " + table + " LIMIT ? OFFSET ?",
                        new Object[] { limit, offset },
                this.rowMapper
                );
    }

    //给子类用的方法
    public void deleteById(long id) {
        getJdbcTemplate().update("DELETE FROM " + table + " WHERE id = ?", id);
    }



    public RowMapper<T> getRowMapper() {
        return this.rowMapper;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getParameterizedType() {
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Class " + getClass().getName() + " does not have parameterized type.");
        }
        ParameterizedType pt = (ParameterizedType) type;
        Type[] types = pt.getActualTypeArguments();
        if (types.length != 1) {
            throw new IllegalArgumentException(
                    "Class " + getClass().getName() + " has more than 1 parameterized types.");
        }
        Type r = types[0];
        if (!(r instanceof Class<?>)) {
            throw new IllegalArgumentException(
                    "Class " + getClass().getName() + " does not have parameterized type of class.");
        }
        return (Class<T>) r;
    }
}
