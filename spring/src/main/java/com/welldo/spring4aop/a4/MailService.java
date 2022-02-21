package com.welldo.spring4aop.a4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * author:welldo
 * date: 2022-02-21 10:34
 */

@Component
public class MailService {

    @Autowired
    UserService userService;

    public String sendMail() {

        //避坑指南,只需要把直接访问字段的代码，改为通过方法访问：
        // ZoneId zoneId = userService.zoneId;
        ZoneId zoneId = userService.getZoneId();


        String dt = ZonedDateTime.now(zoneId).toString();
        return "Hello, it is " + dt;
    }



}
