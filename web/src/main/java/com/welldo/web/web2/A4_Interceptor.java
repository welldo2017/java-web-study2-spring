package com.welldo.web.web2;


/**
 * 4.使用 A4_Interceptor
 *
 * 1.在Web程序中，注意到使用Filter的时候，Filter由Servlet容器管理，它在Spring MVC的Web应用程序中作用范围如下：
 *
 *          │   ▲
 *          ▼   │
 *        ┌───────┐
 *        │Filter1│
 *        └───────┘
 *          │   ▲
 *          ▼   │
 *        ┌───────┐
 * ┌ ─ ─ ─│Filter2│─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐    虚线框就是Filter2的拦截范围
 *        └───────┘
 * │        │   ▲                        │
 *          ▼   │
 * │    ┌─────────────────┐              │
 *      │DispatcherServlet│<───┐
 * │    └─────────────────┘    │         │
 *       │              ┌────────────┐
 * │     │              │ModelAndView│   │
 *       │              └────────────┘
 * │     │                     ▲         │
 *       │    ┌───────────┐    │
 * │     ├───>│Controller1│────┤         │
 *       │    └───────────┘    │
 * │     │                     │         │
 *       │    ┌───────────┐    │
 * │     └───>│Controller2│────┘         │
 *            └───────────┘
 * └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * Filter组件实际上并不知道后续内部处理是通过Spring MVC提供的DispatcherServlet,还是其他Servlet组件
 *
 *
 * 2.spring MVC提供的一种功能类似Filter的拦截器：A4_Interceptor。
 * 和Filter相比，Interceptor拦截范围不是后续整个处理流程，而是仅针对Controller拦截：
 *
 *        │   ▲
 *        ▼   │
 *      ┌───────┐
 *      │Filter1│
 *      └───────┘
 *        │   ▲
 *        ▼   │
 *      ┌───────┐
 *      │Filter2│
 *      └───────┘
 *        │   ▲
 *        ▼   │
 * ┌─────────────────┐
 * │DispatcherServlet│<───┐
 * └─────────────────┘    │
 *  │ ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┼ ─ ─ ─ ┐虚线框就是Interceptor的拦截范围
 *  │                     │
 *  │ │            ┌────────────┐ │
 *  │              │   Render   │
 *  │ │            └────────────┘ │
 *  │                     ▲
 *  │ │                   │       │
 *  │              ┌────────────┐
 *  │ │            │ModelAndView│ │
 *  │              └────────────┘
 *  │ │                   ▲       │
 *  │    ┌───────────┐    │
 *  ├─┼─>│Controller1│────┤       │
 *  │    └───────────┘    │
 *  │ │                   │       │
 *  │    ┌───────────┐    │
 *  └─┼─>│Controller2│────┘       │
 *       └───────────┘
 *    └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 * Interceptor的拦截范围其实就是Controller方法，它实际上就相当于基于AOP的方法拦截
 * 因为Interceptor只拦截Controller方法，所以要注意，返回ModelAndView并渲染后，后续处理就脱离了Interceptor的拦截范围。
 *
 * 3.最后，要让拦截器生效，
 * 我们在 {@link com.welldo.web.web1.AppConfig}的 WebMvcConfigurer（）方法中中注册所有的Interceptor：
 * {@link com.welldo.web.web1.filter.LoggerInterceptor}
 *
 *
 */

public class A4_Interceptor {

}
