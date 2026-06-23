package com.scrns.database;

import com.scrns.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public StudentDAO() {
        createTable();
    }

    // ================= CONNECTION =================
    private Connection getConnection() throws SQLException {
        return DatabaseManager.getConnection();
    }

    // ================= TABLE CREATE =================
    private void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS students(
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL
                )
                """;

        try (
                Connection con = getConnection();
                Statement stmt = con.createStatement()
        ) {
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= CREATE / SAVE =================
    public void saveStudent(Student student) {

        String sql = "INSERT INTO students(id, name) VALUES(?, ?)";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, student.getId());
            ps.setString(2, student.getName());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= READ ALL =================
    public List<Student> getAllStudents() {

        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (
                Connection con = getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                list.add(new Student(
                        rs.getString("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= UPDATE =================

    public void updateStudent(Student student) {

        String sql = "UPDATE students SET name = ? WHERE id = ?";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getName());

            ps.setString(1, student.getName());
            ps.setString(2, student.getId());

            int rows = ps.executeUpdate();

            System.out.println("Rows Updated = " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // ================= DELETE =================
    public void deleteStudent(String id) {

        String sql = "DELETE FROM students WHERE id = ?";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= SEARCH =================
    public List<Student> searchStudents(String keyword) {

        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students WHERE id LIKE ? OR name LIKE ?";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String query = "%" + keyword + "%";

            ps.setString(1, query);
            ps.setString(2, query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Student(
                        rs.getString("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}