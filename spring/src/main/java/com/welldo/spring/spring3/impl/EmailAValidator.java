package com.welldo.spring.spring3.impl;

import com.welldo.spring.spring3.A2_Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * author:welldo
 * date: 2022-01-05 19:40
 */
@Order(1)
@Component
public class EmailAValidator implements A2_Validator {
    //这个实现类,只验证邮箱
    @Override
    public void validate(String email, String password, String name) {
        if (!email.matches("^[a-z0-9]+\\@[a-z0-9]+\\.[a-z]{2,10}$")) {
            throw new IllegalArgumentException("invalid email: " + email);
        }
        System.out.println("----email ok");

    }
}
