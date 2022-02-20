package com.welldo.spring4aop.a2;



/**
 * 1. 一种可行的方式是使用Proxy模式，将某个功能，例如，权限检查，放入Proxy中
 * 这种方式的缺点是比较麻烦，必须先抽取接口，然后，针对每个方法实现Proxy。
 *
 * 代理模式,见
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319432618017
 *
 * 2.另一种方法是，既然SecurityCheckBookService的代码都是标准的Proxy样板代码，
 * 不如把权限检查视作一种切面（Aspect），把日志、事务也视为切面，
 * 然后，以某种自动化的方式，把切面织入到核心逻辑中，实现Proxy模式。
 * 见 a3包
 *
 * author:welldo
 * date: 2022-02-20 17:11
 */
public class Main {


}
