package com.welldo.web.web1.mbean;


import com.welldo.web.web1.AppConfig;
import com.welldo.web.web1.filter.BlacklistInterceptor;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 0.
 * 应用程序使用JMX，只需要两步：
 * a,编写MBean提供管理接口和监控数据；
 * b,注册MBean。
 *
 * 1.在Spring应用程序中，使用JMX只需要第一步：
 * a,编写MBean提供管理接口和监控数据。
 * 第二步注册的过程由Spring自动完成,只需要在 {@link AppConfig} 中加上@EnableMBeanExport注解，告诉Spring注册MBean：
 *
 * 2，JMX的MBean通常以MBean结尾
 * 使用MBean和普通Bean是完全一样的,例如，我们在 {@link BlacklistInterceptor} 对IP进行黑名单拦截：
 *
 *
 */


@Component      //首先是一个标准的Spring管理的Bean
//添加了@ManagedResource表示这是一个MBean，将要被注册到JMX
//objectName指定了这个MBean的名字，通常以company:name=Xxx来分类MBean。
@ManagedResource(objectName = "cmcc:welldo=blacklist", description = "Blacklist of IP addresses")
public class BlacklistMBean {

    private Set<String> ips = new HashSet<>();

    //对于属性，使用@ManagedAttribute注解标注。上述MBean只有get属性，没有set属性，说明这是一个只读属性。
    @ManagedAttribute
    public String[] getBlacklist() {
        String[] array = ips.toArray(new String[ips.size()]);
        return array ;
    }



    //对于操作，使用@ManagedOperation注解标准
    @ManagedOperation
    @ManagedOperationParameter(name = "ip", description = "Target IP address that will be added to blacklist")
    public void addBlacklist(String ip) {
        ips.add(ip);
    }



    //对于操作，使用@ManagedOperation注解标准
    @ManagedOperation
    @ManagedOperationParameter(name = "ip", description = "Target IP address that will be added to blacklist")
    public void removeBlacklist(String ip) {
        ips.remove(ip);
    }


    //其他方法如shouldBlock()不会被暴露给JMX。
    public boolean shouldBlock(String ip) {
        return ips.contains(ip);
    }




}
