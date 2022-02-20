package com.welldo.spring4aop.a1;

/**
 * AOP是Aspect Oriented Programming，即面向切面编程。
 *
 * 1.为何有了aop?
 * xxx1Service()，除了业务逻辑，还需要安全检查、日志记录和事务处理;
 * xxx2Service()，除了业务逻辑，还需要安全检查、日志记录和事务处理...
 *
 * xxxService 关心的是自身的核心逻辑，
 * 但整个系统还要求关注安全检查、日志、事务等功能，这些功能实际上“横跨”多个业务方法，
 * 为了实现这些功能，不得不在每个业务方法上重复编写代码。
 *
 * 2.如何解决?
 * 一种可行的方式是使用Proxy模式，将某个功能，例如，权限检查，放入Proxy中,见 a2包
 *
 * author:welldo
 * date: 2022-02-20 17:09
 */
public class Main {
    public static void main(String[] args) {
        Book book = new Book();
        BookService bookService = new BookService();
        bookService.createBook(book);

    }
}
