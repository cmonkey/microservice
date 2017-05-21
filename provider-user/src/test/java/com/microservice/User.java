package com.microservice;

import java.io.Serializable;

/**
 * Created by cmonkey on 5/21/17.
 */
public class User implements Serializable{
    protected int id;
    protected String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
