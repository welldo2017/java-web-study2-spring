package com.welldo.spring.spring2.service;

import com.welldo.spring.spring2.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * author:welldo
 * date: 2022-01-05 13:19
 */
@Component
public class UserService {

    /**
     * 4. 使用 @Autowired 就相当于把指定类型的Bean注入到指定的字段中。
     * 和XML配置相比，@Autowired大幅简化了注入，因为它不但可以写在字段上，还可以直接写在set()方法上，
     * 甚至可以写在构造方法中：
     *     public UserService(@Autowired MailService mailService) {
     *         this.mailService = mailService;
     *     }
     *
     * ！！！我们一般把@Autowired写在字段上，通常使用package权限的字段，便于测试
     */
    @Autowired   //字段上的 @Autowired 注解，和构造器，二选其一。
    private MailService mailService;

    //字段上的 @Autowired 注解，和构造器，二选其一。
    // public UserService(@Autowired MailService mailService) {
    //     this.mailService = mailService;
    // }


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
        throw new RuntimeException("login failed.");
    }


}
