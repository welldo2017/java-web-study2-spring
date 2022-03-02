package com.welldo.spring.spring8.bean;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


//使用的所有注解均来自javax.persistence，它是JPA规范的一部分
//标注一个@MappedSuperclass表示它用于继承
@MappedSuperclass
public abstract class AbstractEntity {  //有了AbstractEntity，我们就可以大幅简化 javaBean

    private Long id;
    private Long createdAt;


    // 自增主键再追加一个@GeneratedValue，以便Hibernate能读取到自增主键的值。
    //每个属性到数据库列的映射用@Column()标识,nullable指示列是否允许为NULL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id =id;
    }


    @Column(nullable = false, updatable = false)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    //短暂的;临时的,(表示这个字段不持久,不用持久到数据库中)
    //否则Hibernate会尝试从数据库读取名为 createdDateTime 这个不存在的字段,从而出错
    @Transient
    public ZonedDateTime getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
    }

    //表示在我们将一个JavaBean持久化到数据库之前（即执行INSERT语句），Hibernate会先执行该方法
    @PrePersist
    public void preInsert() {
        setCreatedAt(System.currentTimeMillis());
    }
}