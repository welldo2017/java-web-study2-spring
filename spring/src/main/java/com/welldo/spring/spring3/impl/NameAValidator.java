package com.welldo.spring.spring3.impl;

import com.welldo.spring.spring3.A2_Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * author:welldo
 * date: 2022-01-05 19:40
 */
@Order(2)
@Component
public class NameAValidator implements A2_Validator {
    //这个实现类,只验证name
    @Override
    public void validate(String email, String password, String name) {
        if (name == null || name.isEmpty() || name.equals("".toString()) || name.length() > 20) {
            throw new IllegalArgumentException("invalid name: " + name);
        }
        System.out.println("----name ok");

    }
}
