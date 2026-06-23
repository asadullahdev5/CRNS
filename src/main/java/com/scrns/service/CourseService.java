package com.scrns.service;

import com.scrns.database.CourseDAO;

import java.util.List;

public class CourseService {

    private final CourseDAO dao = new CourseDAO();

    // ================= ADD COURSE =================
    public String addCourse(String courseId, String courseName, String capacityStr) {

        if (courseId.isBlank() || courseName.isBlank() || capacityStr.isBlank()) {
            return "Tamam fields fill karo!";
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr.trim());
            if (capacity <= 0) return "Capacity positive honi chahiye!";
        } catch (NumberFormatException e) {
            return "Capacity sirf number hona chahiye!";
        }

        boolean saved = dao.saveCourse(courseId.trim(), courseName.trim(), capacity);
        return saved ? "OK" : "Course ID already exists ya koi error hua!";
    }

    // ================= DELETE COURSE =================
    public void deleteCourse(String courseId) {
        dao.deleteCourse(courseId);
    }

    // ================= GET ALL =================
    public List<String[]> getAllCourses() {
        return dao.getAllCourses();
    }

    // ================= ENROLL =================
    public String enrollStudent(String courseId, String studentId) {

        if (courseId.isBlank() || studentId.isBlank()) {
            return "Course ID aur Student ID dono dalo!";
        }

        int result = dao.enrollStudent(courseId.trim(), studentId.trim());

        return switch (result) {
            case  0  -> "OK";
            case -1  -> "Course nahi mila! ID check karo.";
            case -2  -> "Course full hai! Capacity reach ho gayi.";
            case -3  -> "Student pehle se enroll hai is course mein!";
            case -4  -> "Student nahi mila! Pehle student add karo.";
            default  -> "Kuch masla hua, dobara try karo.";
        };
    }

    // ================= UNENROLL =================
    public void unenrollStudent(String courseId, String studentId) {
        dao.unenrollStudent(courseId, studentId);
    }

    // ================= STUDENTS IN COURSE =================
    public List<String[]> getStudentsInCourse(String courseId) {
        return dao.getEnrollmentsByCourse(courseId);
    }

    // ================= COURSES OF STUDENT =================
    public List<String[]> getCoursesOfStudent(String studentId) {
        return dao.getCoursesByStudent(studentId);
    }
}
