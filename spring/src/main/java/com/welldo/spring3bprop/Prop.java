package com.welldo.spring3bprop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//亲测:这个属性,可以写在configuration上;也可以写在任意一个 component上.
// @PropertySource("app.properties")
@Component
public class Prop {

    @Value("${appname}")
    String appName;
}
