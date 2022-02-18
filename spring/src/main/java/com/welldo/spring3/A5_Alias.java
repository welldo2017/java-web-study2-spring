package com.welldo.spring3;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

/**
 * 别名
 *
 * author:welldo
 * date: 2022-02-18 15:37
 */
@Component
public class A5_Alias  {

    //main方法中,定义了两个 ZoneId的bean,再这里直接注入的话,会报错.expected single matching bean but found 2: z,utc8
    //意思是期待找到唯一的ZoneId类型Bean，但是找到两。

    // 因此，注入时，要指定Bean的名称：
    //方法1.@Qualifier("z") ,指定注入名称为"z"的ZoneId

    //方法2. 是把其中某个Bean指定为@Primary, 在注入时，如果没有使用 @Qualifier指出Bean的名字，Spring会注入标记有@Primary的Bean

    @Qualifier("z")
    @Autowired
    ZoneId zoneId;


}
