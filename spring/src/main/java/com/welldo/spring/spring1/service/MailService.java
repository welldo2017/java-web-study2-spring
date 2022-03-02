package com.welldo.spring.spring1.service;

import com.welldo.spring.spring1.bean.User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 在用户登录和注册成功后发送邮件通知：
 *
 * author:welldo
 * date: 2022-01-05 13:19
 */
public class MailService {
    private ZoneId zoneId = ZoneId.systemDefault();



    public String getTime() {
        return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    //登录成功
    public void sendLoginMail(User user) {
        System.out.println(String.format("----Hi, %s! You are logged in at %s", user.getName(), getTime()));
    }

    //注册
    public void sendRegistrationMail(User user) {
        System.out.println(String.format("----Welcome, %s!", user.getName()));

    }
}
