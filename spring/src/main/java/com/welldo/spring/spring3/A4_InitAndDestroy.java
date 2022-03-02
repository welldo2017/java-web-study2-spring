package com.welldo.spring.spring3;

import com.welldo.spring.spring3.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 初始化和销毁
 * 有些时候，一个Bean在注入必要的依赖后，需要进行初始化（监听消息等）。
 * 在容器关闭时，有时候还需要清理资源（关闭连接池等）。
 * 我们通常会定义一个init()方法进行初始化，定义一个shutdown()方法进行清理，然后，引入JSR-250定义的Annotation：
 *
 * <dependency>
 *     <groupId>javax.annotation</groupId>
 *     <artifactId>javax.annotation-api</artifactId>
 *     <version>1.3.2</version>
 * </dependency>
 *
 * author:welldo
 * date: 2022-01-11 21:28
 */
@Component
public class A4_InitAndDestroy {

    @Autowired(required = false)
    Person person = new Person("4默认创建的person");

    @PostConstruct
    public void init() {
        System.out.println("----初始化和销毁 init" + this.person);
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("----初始化和销毁 Destroy");
    }

}
