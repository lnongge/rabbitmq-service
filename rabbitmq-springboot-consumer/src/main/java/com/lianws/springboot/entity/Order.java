package com.lianws.springboot.entity;

import java.io.Serializable;

/**
 * @Description: 实体类
 * @Author: lianws
 * @Date: 2019/3/25 20:13
 */
public class Order implements Serializable{

    private static final long serialVersionUID = 4506623369684123087L;
    private String id;
    private String name;

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order(String id, String name) {
    
        this.id = id;
        this.name = name;
    }
}
