package com.welldo.spring.spring4aop.a2;



public class SecurityCheckBookService implements BookService {
    private final BookService target;

    //构造器
    public SecurityCheckBookService(BookService target) {
        this.target = target;
    }

    public void createBook(Book book) {//需要安全检查
        securityCheck();
        target.createBook(book);
    }

    public void updateBook(Book book) {//需要安全检查
        securityCheck();
        target.updateBook(book);
    }

    public void deleteBook(Book book) {//需要安全检查
        securityCheck();
        target.deleteBook(book);
    }


    //安全检查,公共方法
    private void securityCheck(){
        System.out.println("安全检查中....");
        System.out.println("安全,通过");
    }
}