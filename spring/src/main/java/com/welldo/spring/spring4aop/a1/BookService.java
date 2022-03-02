package com.welldo.spring.spring4aop.a1;


/**
 * 对于安全检查、日志、事务等代码，它们会重复出现在每个业务方法中。
 * 使用OOP，我们很难将这些四处分散的代码模块化。
 *
 * author:welldo
 * date: 2022-02-08 17:02
 */
public class BookService {
    public void createBook(Book book) {
        securityCheck();
        System.out.println("createBook....");//核心业务
        log("created book: " + book);
    }

    public void updateBook(Book book) {
        securityCheck();
        System.out.println("updateBook....");//核心业务
        log("updated book: " + book);
    }


    //安全检查,公共方法
    private void securityCheck(){
        System.out.println("安全检查中....");
        System.out.println("安全,通过");
    }

    //日志,,公共方法
    private void  log(String str){
        System.out.println("使用log4j写日志中");
    }
}
