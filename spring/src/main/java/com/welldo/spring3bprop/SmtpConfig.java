package com.welldo.spring3bprop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmtpConfig {

    @Value("${smtp.host}")//app.properties中有值
    private String host;

    @Value("${smtp.port:25}")//app.properties中没有值,将使用默认值:25
    private int port;

    //必须要有get方法
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}