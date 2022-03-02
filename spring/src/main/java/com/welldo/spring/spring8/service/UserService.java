package com.welldo.spring.spring8.service;

import com.welldo.spring.spring8.bean.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    //1.增:Insert操作
    public User register(String email, String password, String name) {
        // 创建一个User对象:
        User user = new User();
        // 设置好各个属性:
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        // 不要设置id，因为使用了自增主键
        // 保存到数据库:
        hibernateTemplate.save(user);
        // 现在已经自动获得了id:
        System.out.println(user.getId());
        return user;
    }

    /**
     * 2.删:Delete操作
     * 注意:Hibernate总是用id来删除记录
     *
     * load()和get()都可以根据主键加载记录，它们的区别在于，当记录不存在时，get()返回null，而load()抛出异常。
     */
    public boolean deleteUser(Long id) {
        User user = hibernateTemplate.get(User.class, id);
        if (user != null) {
            hibernateTemplate.delete(user);
            return true;
        }
        return false;
    }


    /**
     * 3.改
     * Hibernate在更新记录时，它只会把@Column(updatable=true)的属性加入到UPDATE语句中，
     * 这样可以提供一层额外的安全性，
     * 即如果不小心修改了User的email、createdAt等属性，执行update()时并不会更新对应的数据库列
     */
    public void updateUser(Long id, String name) {
        User user = hibernateTemplate.load(User.class, id);
        user.setName(name);
        hibernateTemplate.update(user);
    }

    /**
     * 4.查
     * 根据id查询我们可以直接调用load()或get()，如果要使用条件查询，有3种方法
     *
     * 4.1 使用Example查询
     * SELECT * FROM user WHERE email = ? AND password = ?
     *
     * 注意:使用findByExample()时，注意基本类型字段总是会加入到WHERE条件！
     * 如果把User的createdAt的类型从Long改为long，findByExample()的查询将出问题，原因在于example实例的long类型字段有了默认值0，
     */
    public User login(String email, String password) {
        User example = new User();
        example.setEmail(email);
        example.setPassword(password);
        List<User> list = hibernateTemplate.findByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }




    /**
     * 4.2 使用Criteria查询
     * 第二种查询方法是使用Criteria查询，可以实现如下
     * SELECT * FROM user WHERE email = ? AND password = ?
     *
     * 和findByExample()相比，findByCriteria()可以组装出更灵活的WHERE条件，
     */
    public User login2(String email, String password) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password));
        List<User> list = (List<User>) hibernateTemplate.findByCriteria(criteria);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 4.3  最后一种常用的查询是直接编写Hibernate内置的HQL查询：
     * List<User> list = (List<User>) hibernateTemplate.find("FROM User WHERE email=? AND password=?", email, password);
     * 和SQL相比，HQL使用类名和属性名，由Hibernate自动转换为实际的表名和列名
     */




    //------------------------------其他的方法 ------------------------------
    public User getUserById(long id) {
        return hibernateTemplate.load(User.class, id);
    }

    public User fetchUserByEmail(String email) {
        User example = new User();
        example.setEmail(email);
        List<User> list = hibernateTemplate.findByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }
    public User getUserByEmail(String email) {
        User user = fetchUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("user not found by email: " + email);
        }
        return user;
    }
    @SuppressWarnings("unchecked")
    public List<User> getUsers(int pageIndex) {
        int pageSize = 100;
        return (List<User>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(User.class),
                (pageIndex - 1) * pageSize, pageSize);
    }
    public User signin(String email, String password) {
        @SuppressWarnings({ "deprecation", "unchecked" })
        List<User> list = (List<User>) hibernateTemplate.find(
                "FROM User WHERE email=? AND password=?",
                email,
                password);
        return list.isEmpty() ? null : list.get(0);
    }



}