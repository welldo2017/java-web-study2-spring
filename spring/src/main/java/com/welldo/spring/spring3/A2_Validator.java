package com.welldo.spring.spring3;

/**
 * 有些时候，我们会有一系列接口相同，不同实现类的Bean。
 * 例如，注册用户时，我们要对email、password和name这3个变量进行验证。
 * 为了便于扩展，我们先定义验证接口：
 *
 * author:welldo
 * date: 2022-01-05 19:37
 */

//接口不需要设置为 @Component
public interface A2_Validator {

    void validate(String email, String password, String name);
}
