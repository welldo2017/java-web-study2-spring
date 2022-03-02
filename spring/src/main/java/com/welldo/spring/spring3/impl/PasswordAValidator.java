package com.welldo.spring.spring3.impl;

import com.welldo.spring.spring3.A2_Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * author:welldo
 * date: 2022-01-05 19:40
 */

@Order(3)
@Component
public class PasswordAValidator implements A2_Validator {
    @Override
    public void validate(String email, String password, String name) {
        if (!password.matches("^.{6,20}$")) {
            throw new IllegalArgumentException("invalid password");
        }
        System.out.println("----pwd ok");
    }
}
