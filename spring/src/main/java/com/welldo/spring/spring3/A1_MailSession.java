package com.welldo.spring.spring3;

import org.springframework.stereotype.Component;

/**
 * 1. 对于Spring容器来说，当我们把一个Bean标记为@Component后，它就会自动为我们创建一个单例（Singleton），
 * 即容器初始化时创建Bean，容器关闭前销毁Bean。
 * 在容器运行期间，我们调用getBean(Class)获取到的Bean总是同一个实例。
 * 看代码 {@link Main}
 *
 * 还有一种Bean，我们每次调用getBean(Class)，容器都返回一个新的实例，这种Bean称为Prototype（原型），
 * ！！！没事别用Prototype
 *
 * author:welldo
 * date: 2022-01-05 19:35
 */


//声明一个Prototype的Bean时，需要添加一个额外的@Scope注解：二者任选其一
// @Scope("prototype")
// @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class A1_MailSession {
}
