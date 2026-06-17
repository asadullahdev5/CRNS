package com.scrns.model;

import java.io.Serializable;

public abstract class User implements Serializable {

    protected String id;
    protected String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();
}