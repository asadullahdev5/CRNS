package com.scrns.persistence;

import com.scrns.model.Course;

import java.io.*;

public class DataManager {

    public static void saveCourse(Course course){

        try(ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream("course.dat"))){

            out.writeObject(course);

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    public static Course loadCourse(){

        try(ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream("course.dat"))){

            return (Course) in.readObject();

        }catch(Exception e){

            return null;
        }
    }
}