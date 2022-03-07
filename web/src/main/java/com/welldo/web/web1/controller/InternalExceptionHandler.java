package com.welldo.web.web1.controller;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 5.b
 * https://howtodoinjava.com/spring-core/spring-exceptionhandler-annotation/#4
 *
 * 答案1：
 * 写一个类,里面写上 handleUnknowException（）方法，标注上@ControllerAdvice，
 * 等效于在所有的Controller里写上了 handleUnknowException（）方法
 *
 * 答案2：
 * 在父类写个handleUnknowException()就可以了
 * 因为controller实际上按url分类不会太多，统一继承base-controller 是完全可行的，公共方法也放base-controller
 *
 */
@ControllerAdvice
public class InternalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnknowException(Exception ex) {
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.getClass().getSimpleName());
        map.put("message",ex.getMessage());
        map.put("where","InternalExceptionHandler");
        return new ModelAndView("500.html", map);
    }


}
