package com.scrns.database;

import com.scrns.model.Course;
import com.scrns.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public CourseDAO() {
        createTables();
    }

    // ================= CONNECTION =================
    private Connection getConnection() throws SQLException {
        return DatabaseManager.getConnection();
    }

    // ================= TABLE CREATE =================
    private void createTables() {

        String courseTable = """
                CREATE TABLE IF NOT EXISTS courses (
                    course_id   TEXT PRIMARY KEY,
                    course_name TEXT NOT NULL,
                    capacity    INTEGER NOT NULL
                )
                """;

        String enrollTable = """
                CREATE TABLE IF NOT EXISTS enrollments (
                    course_id  TEXT NOT NULL,
                    student_id TEXT NOT NULL,
                    PRIMARY KEY (course_id, student_id),
                    FOREIGN KEY (course_id)  REFERENCES courses(course_id)  ON DELETE CASCADE,
                    FOREIGN KEY (student_id) REFERENCES students(id)        ON DELETE CASCADE
                )
                """;

        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(courseTable);
            stmt.execute(enrollTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= SAVE COURSE =================
    public boolean saveCourse(String courseId, String courseName, int capacity) {

        String sql = "INSERT INTO courses(course_id, course_name, capacity) VALUES(?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ps.setString(2, courseName);
            ps.setInt(3, capacity);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= GET ALL COURSES =================
    public List<String[]> getAllCourses() {

        List<String[]> list = new ArrayList<>();
        String sql = """
                SELECT c.course_id, c.course_name, c.capacity,
                       COUNT(e.student_id) AS enrolled
                FROM courses c
                LEFT JOIN enrollments e ON c.course_id = e.course_id
                GROUP BY c.course_id
                """;

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("capacity"),
                        rs.getString("enrolled")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= DELETE COURSE =================
    public void deleteCourse(String courseId) {

        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= ENROLL STUDENT =================
    /**
     * Returns:
     *  0 = success
     * -1 = course not found
     * -2 = course full
     * -3 = already enrolled
     * -4 = student not found
     */
    public int enrollStudent(String courseId, String studentId) {

        // Check course exists & get capacity
        String checkCourse = "SELECT capacity FROM courses WHERE course_id = ?";
        String countEnrolled = "SELECT COUNT(*) FROM enrollments WHERE course_id = ?";
        String checkStudent = "SELECT id FROM students WHERE id = ?";
        String checkDuplicate = "SELECT 1 FROM enrollments WHERE course_id = ? AND student_id = ?";
        String insertSql = "INSERT INTO enrollments(course_id, student_id) VALUES(?, ?)";

        try (Connection con = getConnection()) {

            // 1. Student exists?
            try (PreparedStatement ps = con.prepareStatement(checkStudent)) {
                ps.setString(1, studentId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return -4;
            }

            // 2. Course exists?
            int capacity;
            try (PreparedStatement ps = con.prepareStatement(checkCourse)) {
                ps.setString(1, courseId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return -1;
                capacity = rs.getInt("capacity");
            }

            // 3. Already enrolled?
            try (PreparedStatement ps = con.prepareStatement(checkDuplicate)) {
                ps.setString(1, courseId);
                ps.setString(2, studentId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return -3;
            }

            // 4. Course full?
            try (PreparedStatement ps = con.prepareStatement(countEnrolled)) {
                ps.setString(1, courseId);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) >= capacity) return -2;
            }

            // 5. Enroll!
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setString(1, courseId);
                ps.setString(2, studentId);
                ps.executeUpdate();
            }

            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -99;
        }
    }

    // ================= UNENROLL STUDENT =================
    public void unenrollStudent(String courseId, String studentId) {

        String sql = "DELETE FROM enrollments WHERE course_id = ? AND student_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ps.setString(2, studentId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= GET ENROLLMENTS FOR COURSE =================
    public List<String[]> getEnrollmentsByCourse(String courseId) {

        List<String[]> list = new ArrayList<>();
        String sql = """
                SELECT s.id, s.name
                FROM students s
                JOIN enrollments e ON s.id = e.student_id
                WHERE e.course_id = ?
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new String[]{rs.getString("id"), rs.getString("name")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET COURSES FOR STUDENT =================
    public List<String[]> getCoursesByStudent(String studentId) {

        List<String[]> list = new ArrayList<>();
        String sql = """
                SELECT c.course_id, c.course_name, c.capacity
                FROM courses c
                JOIN enrollments e ON c.course_id = e.course_id
                WHERE e.student_id = ?
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("capacity")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}