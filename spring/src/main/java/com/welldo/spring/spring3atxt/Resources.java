package com.welldo.spring.spring3atxt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Component
public class Resources {

    //直接把 int 1 注入给version属性
    @Value("1")
    private int version;

    //注意:它是以文本形式读进来的,不能自动转成kv形式,就好比txt
    //对程序员来讲,还需要手动将文本转成kv,

    //注入Resource最常用的方式是通过classpath，即类似classpath:/logo.txt表示在classpath中搜索logo.txt文件，
    // 然后，我们直接调用Resource.getInputStream()就可以获取到输入流，避免了自己搜索文件的代码。
    @Value("classpath:/logo.txt")
    private Resource resource;

    private String logo;

    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        ) {

            this.logo = reader.lines().collect(Collectors.joining("\n"));
        }
    }


    public void printLogo() {
        System.out.println(logo);
        System.out.println("app.version: " + version);
    }
}
