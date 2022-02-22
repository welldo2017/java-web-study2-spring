package com.welldo.spring8.bean;


import javax.persistence.*;

/**
 *
 */


// 使用的所有注解均来自javax.persistence，它是JPA规范的一部分
@Entity
public class User extends AbstractEntity {

    // private Long id;
    // private Long createdAt;

    private String email;
    private String password;
    private String name;

    //构造器
    public User() {
    }

    public User(long id, String email, String password, String name) {
        setId(id);
        setEmail(email);
        setPassword(password);
        setName(name);
    }


    //updatable指示该列是否允许被用在UPDATE语句
    @Column(nullable = false, unique = true, length = 100)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    @Column(nullable = false, length = 100)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    //length指示String类型的列的长度（如果没有指定，默认是255）。
    @Column(nullable = false, length = 100)
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
                getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
    }




}
