package com.welldo.web;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * 用来启动一个tomcat
 *
 * author:welldo
 * date: 2022-03-03 14:15
 */
public class Main {

    //账密 3@qq.com / 111222
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("", new File("web/src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        new File("web/target/classes").getAbsolutePath(),
                        "/"
                )
        );
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }


}
