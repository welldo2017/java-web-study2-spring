package com.welldo.spring2.service;

import com.welldo.spring2.bean.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 3. 这个@Component注解就相当于定义了一个Bean，它有一个可选的名称，默认是mailService，即小写开头的类名。
 *
 * 有些时候，一个Bean在注入必要的依赖后，需要进行初始化（监听消息等）。
 * 在容器关闭时，有时候还需要清理资源（关闭连接池等）。
 * 我们通常会定义一个init()方法进行初始化，定义一个shutdown()方法进行清理，
 * 然后，引入JSR-250定义的Annotation： * (查看pom javax.annotation-api)
 * 在Bean的初始化和清理方法上标记@PostConstruct和@PreDestroy：
 *
 * author:welldo
 * date: 2022-01-05 13:19
 */
@Component
public class MailService {

    //Spring只根据Annotation查找无参数方法，对方法名不作要求。
    @PostConstruct
    public void init() {
        System.out.println("--------Init mailService with zoneId = " + this.zoneId);
    }

    //Spring只根据Annotation查找无参数方法，对方法名不作要求。
    @PreDestroy
    public void shutdown() {
        System.out.println("-------Shutdown mailService");
    }


    ZoneId zoneId = ZoneId.systemDefault();



    public String getTime() {
        return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public void sendLoginMail(User user) {
        System.out.println(String.format("Hi, %s! You are logged in at %s", user.getName(), getTime()));
    }


}
