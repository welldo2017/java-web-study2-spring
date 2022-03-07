package com.welldo.web.web1.controller;


import com.welldo.web.web1.bean.User;
import com.welldo.web.web1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 使用Spring MVC开发Web应用程序的主要工作就是编写Controller逻辑
 *
 * 2.在Web应用中，除了需要使用 MVC 给用户显示页面外，（也就是返回 modelAndView）
 * 还有一类API接口，我们称之为REST，通常输入输出都是JSON，便于第三方调用或者使用页面JavaScript与之交互。
 *
 * 3.
 * 直接在Controller中处理JSON是可以的，因为Spring MVC的@GetMapping和@PostMapping都支持指定输入和输出的格式。
 * 如果我们想接收JSON，输出JSON，那么可以这样写：
 *     @ResponseBody
 *     @PostMapping(value = "/test",
 *             consumes = "application/json;charset=UTF-8",
 *             produces = "application/json;charset=UTF-8")
 *  注意到@PostMapping使用consumes声明能接收的类型，使用produces声明输出的类型，
 *  并且额外加了@ResponseBody表示返回的String无需额外处理，直接作为输出内容写入HttpServletResponse。
 *  输入的JSON则根据注解@RequestBody直接被Spring反序列化为User这个JavaBean。
 *
 * 4.
 * 获取一个user对象的时候， * User能被正确地序列化为JSON，但暴露了password属性，这是我们不期望的。
 * 要避免输出password属性，可以把User复制到另一个UserBean对象，该对象只持有必要的属性，但这样做比较繁琐。
 * 另一种简单的方法是直接在User的password属性定义处加上@JsonIgnore表示完全忽略该属性：
 *
 * 但是这样一来，如果写一个register(User user)方法，那么该方法的User对象也拿不到注册时用户传入的密码了。
 *
 * 如果要允许输入password，但不允许输出password，
 * 即在JSON序列化和反序列化时，允许写属性，禁用读属性，可以更精细地控制如下：
 *  @JsonProperty(access = Access.WRITE_ONLY)
 *   public String getPassword() { *         return password; *     }
 *
 * 5.扩展，RESTful API 设计指南
 * https://www.ruanyifeng.com/blog/2014/05/restful_api.html
 *
 */
@RestController
@RequestMapping("/rest")
@Controller
public class RestTestController {

    @Autowired
    UserService userService;

    @ResponseBody
    @PostMapping(value = "/test",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public String testRest(@RequestBody User user) {
        System.out.println(user);

        return "{\"restSupport\":true}";
    }



    //登录
    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SignInRequest signinRequest) {
        try {
            User user = userService.signin(signinRequest.email, signinRequest.password);
            HashMap<String, Object> map = new HashMap<>();
            map.put("user",user);
            return map;
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("error","SIGNIN_FAILED");
            map.put("message",e.getMessage());
            return map;
        }
    }


    public static class SignInRequest {
        public String email;
        public String password;
    }

}
