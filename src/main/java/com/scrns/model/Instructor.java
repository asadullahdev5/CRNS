package com.scrns.model;

public class Instructor extends User{

    public Instructor(String id,String name){
        super(id,name);
    }

    @Override
    public String getRole() {
        return "Instructor";
    }
}