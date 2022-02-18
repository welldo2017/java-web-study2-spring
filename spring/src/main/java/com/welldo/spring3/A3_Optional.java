package com.welldo.spring3;

import com.welldo.spring3.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 可选注入
 *
 * author:welldo
 * date: 2022-01-05 19:35
 */
@Component
public class A3_Optional {

    /**
     * 3. equired = false 这个参数告诉Spring容器，如果找到一个类型为 person 的Bean，就注入，
     * 如果找不到，就使用默认值,也是就 new出来的。
     * 这种方式非常适合有定义就使用定义，没有就使用默认值的情况。
     */
    @Autowired(required = false)
    Person person = new Person("默认创建的person 3");

}
