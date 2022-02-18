package com.welldo.spring3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 验证的入口
 * author:welldo
 * date: 2022-01-05 19:47
 */
@Component
public class A2_ValidatorEntry {

    /**
     * 注意到 A2_ValidatorEntry 被注入了一个 List<A2_Validator>，
     * Spring会自动把所有类型为 A2_Validator 的Bean装配为一个List注入进来，
     * 这样一来，我们每新增一个 A2_Validator 类型，就自动被Spring装配到 A2_ValidatorEntry 中了，非常方便。
     *
     * 因为Spring是通过扫描classpath获取到所有的Bean，而List是有序的，要指定List中Bean的顺序，可以加上@Order注解：
     */
    @Autowired
    List<A2_Validator> validators;

    public void validate(String email, String password, String name) {
        for (A2_Validator validator : this.validators) {
            validator.validate(email, password, name);
        }
    }
}
