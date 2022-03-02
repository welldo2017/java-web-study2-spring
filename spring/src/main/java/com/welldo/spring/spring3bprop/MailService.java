package com.welldo.spring.spring3bprop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    /**
     * 注意观察#{}这种注入语法，它和${key}不同的是，#{}表示从JavaBean读取属性。
     * "#{smtpConfig.host}"的意思是，从名称为smtpConfig的Bean读取host属性，即调用getHost()方法。
     * 所以, {@link SmtpConfig} 必须要有get方法
     * 一个Class名为 SmtpConfig的Bean，它在Spring容器中的默认名称就是smtpConfig，除非用@Qualifier指定了名称。
     *
     *
     * 使用一个独立的JavaBean持有所有属性，然后在其他Bean中以#{bean.property}注入的好处是，
     * 多个Bean都可以引用同一个Bean的某个属性。
     * 例如，如果SmtpConfig决定从数据库中读取相关配置项，
     * 那么MailService注入的@Value("#{smtpConfig.host}")仍然可以不修改正常运行。
     */
    @Value("#{smtpConfig.host}")
     String smtpHost;

    @Value("#{smtpConfig.port}")
     int smtpPort;
}
