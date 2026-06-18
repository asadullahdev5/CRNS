package com.scrns.service;

import com.scrns.database.StudentDAO;
import com.scrns.model.Student;

import java.util.List;

public class StudentService {

    private StudentDAO dao = new StudentDAO();

    public boolean addStudent(Student s) {

        if (s.getId().isEmpty() || s.getName().isEmpty()) {
            return false;
        }

        dao.saveStudent(s);
        return true;
    }

    public void updateStudent(Student s) {
        dao.updateStudent(s);
    }

    public void deleteStudent(String id) {
        dao.deleteStudent(id);
    }

    public List<Student> getAll() {
        return dao.getAllStudents();
    }

    public List<Student> search(String keyword) {
        return dao.searchStudents(keyword);
    }
}