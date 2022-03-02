package com.welldo.spring.spring4aop.a4;

import org.springframework.stereotype.Component;

import java.time.ZoneId;

/**
 * author:welldo
 * date: 2022-02-21 10:33
 */


@Component
public class UserService {

    // 成员变量:
    public final ZoneId zoneId = ZoneId.systemDefault();

    // 构造方法:
    public UserService() {
        System.out.println("UserService()构造器: init...");
        System.out.println("UserService()构造器: zoneId = " + this.zoneId);
    }

    // public方法:
    public ZoneId getZoneId() {
        return zoneId;
    }


}
