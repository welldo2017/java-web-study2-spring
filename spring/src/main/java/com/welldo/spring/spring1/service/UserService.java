package com.welldo.spring.spring1.service;

import com.welldo.spring.spring1.Main;
import com.welldo.spring.spring1.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * !!!注入的方式,可以通过set()方法实现,也可以通过 构造方法实现
 *
 * 实现用户注册和登录
 *
 * author:welldo
 * date: 2022-01-05 13:19
 */
public class UserService {

    //需要一个MailService
    private MailService mailService;

    /**
     * 注入的方式,可以通过set()方法实现,也可以通过 构造方法实现, 这里是set()方法
     * 然后,通过 application.xml, 告诉Spring的IoC容器应该如何创建并组装Bean：
     * 最后,查看 {@link Main}
     *
     * 疑问1, 既然有了这一段,为何xml中还要重复定义一遍呢?
     * set（）方法，是需要被调用后，才能真正的注入属性。
     * xml中利用反射，也或者不是反射，完成了真正调用这个set方法，完成了属性的注入。
     */
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    //模拟数据库
    private List<User> users = null;
    {
        users = new ArrayList<>();
        users.add(new User(1, "bob@example.com", "password", "Bob"));
        users.add(new User(2, "alice@example.com", "password", "Alice"));
        users.add(new User(3, "tom@example.com", "password", "Tom"));
    }


    /**
     * 这个方法不是真的查询数据库     * 而是通过for循环，找到用户
     */
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                mailService.sendLoginMail(user);
                return user;
            }
        }
        throw new RuntimeException("----login failed.");
    }

}
