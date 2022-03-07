package com.welldo.web.web1.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * 1.
 * 一个Interceptor必须实现HandlerInterceptor接口，
 * 可以选择实现preHandle()、postHandle()和afterCompletion()方法。
 *
 *
 * 2.
 * preHandle()是Controller方法调用前执行，
 * postHandle()是Controller方法正常返回后执行，
 * afterCompletion()无论Controller方法是否抛异常都会执行，参数ex就是Controller方法抛出的异常（未抛出异常是null）。
 */


@Order(1)
@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //在preHandle()中，也可以直接处理响应，然后返回false表示无需调用Controller方法继续处理了，
    // 通常在认证或者安全检查失败时直接返回错误响应。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        logger.info("preHandle {}...", request.getRequestURI());
        if (request.getParameter("debug") != null) {
            PrintWriter pw = response.getWriter();
            pw.write("<p>DEBUG MODE</p>");
            pw.flush();
            return false;
        }
        return true;
    }


    /**
     * 在postHandle()中，因为捕获了Controller方法返回的ModelAndView，
     * 所以可以继续往ModelAndView里添加一些通用数据，
     * 很多页面需要的全局数据如Copyright信息等都可以放到这里，无需在每个Controller方法中重复添加。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        logger.info("postHandle {}.", request.getRequestURI());
        if (modelAndView != null) {
            //！！！注意
            //请查看  profile.html 或者 index.html 页面
            //此页面渲染了 __time__ 这个对象
            modelAndView.addObject("__time__", LocalDateTime.now());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("afterCompletion {}: exception = {}", request.getRequestURI(), ex);
    }




}
