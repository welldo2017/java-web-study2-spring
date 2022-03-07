package com.welldo.web.web1.filter;

import com.welldo.web.web1.controller.UserController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 在 {@link UserController} 的 doRegister（）方法中，注册时，输入中文会导致乱码。
 * 因为Servlet默认按非UTF-8编码读取参数
 *
 * 为了修复这一问题，
 * 1.我们可以简单地使用一个EncodingFilter，
 * 在全局范围类给HttpServletRequest和HttpServletResponse强制设置为UTF-8编码。
 *
 * 2.也可以直接使用Spring MVC自带的一个CharacterEncodingFilter。配置Filter时，只需在web.xml中声明即可：
 *     <filter>
 *         <filter-name>encodingFilter</filter-name>
 *         <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
 *         <init-param>
 *             <param-name>encoding</param-name>
 *             <param-value>UTF-8</param-value>
 *         </init-param>
 *         <init-param>
 *             <param-name>forceEncoding</param-name>
 *             <param-value>true</param-value>
 *         </init-param>
 *     </filter>
 *
 *     <filter-mapping>
 *         <filter-name>encodingFilter</filter-name>
 *         <url-pattern>/*</url-pattern>
 *     </filter-mapping>
 * 这种Filter和我们业务关系不大，注意到CharacterEncodingFilter其实和Spring的IoC容器没有任何关系，
 * 两者均互不知晓对方的存在
 */
@WebFilter(urlPatterns = "/*")
public class Encoding implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("Encoding filter");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request,response);
    }
}
