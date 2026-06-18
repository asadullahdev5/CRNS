package com.scrns.model;

public class Student extends User {

    public Student(String id, String name) {
        super(id, name);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    // 👇 ADD THIS
    public String getId() {
        return super.id; // only works if id is protected
    }
}