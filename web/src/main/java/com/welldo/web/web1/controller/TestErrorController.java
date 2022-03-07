package com.welldo.web.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * 5
 * 在Controller中，Spring MVC还允许定义基于@ExceptionHandler注解的异常处理方法
 *
 *
 * a.使用ExceptionHandler时，要注意它仅作用于当前的Controller，
 * 即ControllerA中定义的一个ExceptionHandler方法对ControllerB不起作用，看代码
 *
 * b. * 如果我们有很多Controller，每个Controller都需要处理一些通用的异常，例如LoginException，
 * 思考一下应该怎么避免重复代码？
 * {@link InternalExceptionHandler}
 *
 */
@Controller
public class TestErrorController {


    /**
     * 5.a
     * 异常处理方法没有固定的方法签名，可以传入Exception、HttpServletRequest等，
     * 返回值可以是void，也可以是ModelAndView，
     *
     * 代码通过@ExceptionHandler(RuntimeException.class)表示当发生RuntimeException的时候，就自动调用此方法处理。
     *
     * 我们返回了一个新的ModelAndView，这样在应用程序内部如果发生了预料之外的异常，可以给用户显示一个出错页面
     */

    // @ExceptionHandler(RuntimeException.class)
    // public ModelAndView handleUnknowException(Exception ex) {
    //     Map<String, String> map = new HashMap<>();
    //     map.put("error", ex.getClass().getSimpleName());
    //     map.put("message",ex.getMessage());
    //     map.put("where","TestErrorController");
    //     return new ModelAndView("500.html", map);
    // }



    //这个方法，会抛异常。
    @GetMapping("/user/profile")
    public ModelAndView user() {

        int i = 1 / 0;
        return new ModelAndView("register.html");
    }

}
