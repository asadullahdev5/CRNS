package com.scrns.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    private String courseName;
    private int capacity;

    private ArrayList<Student> students;

    public Course(String courseName,int capacity){

        this.courseName=courseName;
        this.capacity=capacity;

        students=new ArrayList<>();
    }

    public void registerStudent(Student student)
            throws CourseFullException {

        if(students.size()>=capacity){

            throw new CourseFullException(
                    "Course Capacity Reached!"
            );
        }

        students.add(student);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public String getCourseName() {
        return courseName;
    }
}