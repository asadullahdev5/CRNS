package com.scrns.gui;

import com.scrns.model.Student;
import com.scrns.service.CourseService;
import com.scrns.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    // Student Tab
    private JTextField txtId, txtName;
    private JTable studentTable;
    private DefaultTableModel studentModel;
    private final StudentService studentService = new StudentService();

    // Course Tab
    private JTextField txtCourseId, txtCourseName, txtCapacity;
    private JTable courseTable;
    private DefaultTableModel courseModel;
    private final CourseService courseService = new CourseService();

    // Enrollment Tab
    private JTextField txtEnrollCourseId, txtEnrollStudentId;
    private JTable enrollTable;
    private DefaultTableModel enrollModel;

    // Student Courses Tab
    private JTextField txtStudentLookupId;
    private JTable studentCourseTable;
    private DefaultTableModel studentCourseModel;

    public MainFrame() {

        setTitle("SCRNS - Student Course Registration System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("👤 Students", buildStudentPanel());
        tabs.addTab("📚 Courses", buildCoursePanel());
        tabs.addTab("📋 Enrollment", buildEnrollmentPanel());
        tabs.addTab("🔍 My Courses", buildStudentCoursePanel());

        add(tabs);
        setVisible(true);
    }

    // =========================================================
    // STUDENTS TAB
    // =========================================================

    private JPanel buildStudentPanel() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 3, 8, 8));

        txtId = new JTextField();
        txtName = new JTextField();

        JButton btnAdd = new JButton("➕ Add");
        JButton btnUpdate = new JButton("✏️ Update");
        JButton btnDelete = new JButton("🗑 Delete");
        JButton btnSearch = new JButton("🔍 Search");
        JButton btnLoad = new JButton("🔄 Load All");

        form.add(new JLabel("Student ID"));
        form.add(txtId);
        form.add(btnSearch);

        form.add(new JLabel("Student Name"));
        form.add(txtName);
        form.add(btnAdd);

        form.add(btnUpdate);
        form.add(btnDelete);
        form.add(btnLoad);

        studentModel = new DefaultTableModel(
                new String[]{"Student ID", "Student Name"}, 0);

        studentTable = new JTable(studentModel);

        loadStudentTable();

        root.add(form, BorderLayout.NORTH);
        root.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int row = studentTable.getSelectedRow();

            if (row != -1) {
                txtId.setText(studentModel.getValueAt(row, 0).toString());
                txtName.setText(studentModel.getValueAt(row, 1).toString());
            }
        });

        btnAdd.addActionListener(e -> {

            Student student = new Student(
                    txtId.getText().trim(),
                    txtName.getText().trim()
            );

            if (studentService.addStudent(student)) {
                showMsg("✅ Student added successfully!");
                loadStudentTable();
            } else {
                showMsg("⚠️ Please fill all required fields!");
            }
        });

        btnUpdate.addActionListener(e -> {

            studentService.updateStudent(
                    new Student(
                            txtId.getText().trim(),
                            txtName.getText().trim()
                    )
            );

            loadStudentTable();
            showMsg("✅ Student updated successfully!");
        });

        btnDelete.addActionListener(e -> {

            studentService.deleteStudent(txtId.getText().trim());

            loadStudentTable();
            showMsg("✅ Student deleted successfully!");
        });

        btnSearch.addActionListener(e -> {

            studentModel.setRowCount(0);

            for (Student student :
                    studentService.search(txtId.getText().trim())) {

                studentModel.addRow(
                        new Object[]{
                                student.getId(),
                                student.getName()
                        }
                );
            }
        });

        btnLoad.addActionListener(e -> loadStudentTable());

        return root;
    }

    private void loadStudentTable() {

        studentModel.setRowCount(0);

        for (Student student : studentService.getAll()) {

            studentModel.addRow(
                    new Object[]{
                            student.getId(),
                            student.getName()
                    }
            );
        }
    }

    // =========================================================
    // COURSES TAB
    // =========================================================

    private JPanel buildCoursePanel() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 3, 8, 8));

        txtCourseId = new JTextField();
        txtCourseName = new JTextField();
        txtCapacity = new JTextField();

        JButton btnAdd = new JButton("➕ Add Course");
        JButton btnDelete = new JButton("🗑 Delete Course");
        JButton btnLoad = new JButton("🔄 Load All");

        form.add(new JLabel("Course ID"));
        form.add(txtCourseId);
        form.add(btnAdd);

        form.add(new JLabel("Course Name"));
        form.add(txtCourseName);
        form.add(btnDelete);

        form.add(new JLabel("Course Capacity"));
        form.add(txtCapacity);
        form.add(btnLoad);

        courseModel = new DefaultTableModel(
                new String[]{
                        "Course ID",
                        "Course Name",
                        "Capacity",
                        "Enrolled Students"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(courseModel);

        loadCourseTable();

        root.add(form, BorderLayout.NORTH);
        root.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        courseTable.getSelectionModel().addListSelectionListener(e -> {

            int row = courseTable.getSelectedRow();

            if (row != -1) {

                txtCourseId.setText(
                        courseModel.getValueAt(row, 0).toString());

                txtCourseName.setText(
                        courseModel.getValueAt(row, 1).toString());

                txtCapacity.setText(
                        courseModel.getValueAt(row, 2).toString());
            }
        });

        btnAdd.addActionListener(e -> {

            String result = courseService.addCourse(
                    txtCourseId.getText(),
                    txtCourseName.getText(),
                    txtCapacity.getText()
            );

            if ("OK".equals(result)) {

                showMsg("✅ Course added successfully!");
                loadCourseTable();

            } else {

                showMsg("⚠️ " + result);
            }
        });

        btnDelete.addActionListener(e -> {

            String id = txtCourseId.getText().trim();

            if (id.isBlank()) {

                showMsg("⚠️ Please select a course first!");
                return;
            }

            courseService.deleteCourse(id);

            loadCourseTable();

            showMsg("✅ Course deleted successfully!");
        });

        btnLoad.addActionListener(e -> loadCourseTable());

        return root;
    }

    private void loadCourseTable() {

        courseModel.setRowCount(0);

        for (String[] row : courseService.getAllCourses()) {
            courseModel.addRow(row);
        }
    }

    // =========================================================
    // ENROLLMENT TAB
    // =========================================================

    private JPanel buildEnrollmentPanel() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 3, 8, 8));

        txtEnrollCourseId = new JTextField();
        txtEnrollStudentId = new JTextField();

        JButton btnEnroll = new JButton("✅ Enroll");
        JButton btnUnenroll = new JButton("❌ Unenroll");
        JButton btnView = new JButton("👁 View Students");

        form.add(new JLabel("Course ID"));
        form.add(txtEnrollCourseId);
        form.add(btnEnroll);

        form.add(new JLabel("Student ID"));
        form.add(txtEnrollStudentId);
        form.add(btnUnenroll);

        form.add(new JLabel(""));
        form.add(new JLabel(""));
        form.add(btnView);

        enrollModel = new DefaultTableModel(
                new String[]{
                        "Student ID",
                        "Student Name"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrollTable = new JTable(enrollModel);

        JLabel statusLabel = new JLabel(" ");

        root.add(form, BorderLayout.NORTH);
        root.add(new JScrollPane(enrollTable), BorderLayout.CENTER);
        root.add(statusLabel, BorderLayout.SOUTH);

        enrollTable.getSelectionModel().addListSelectionListener(e -> {

            int row = enrollTable.getSelectedRow();

            if (row != -1) {

                txtEnrollStudentId.setText(
                        enrollModel.getValueAt(row, 0).toString());
            }
        });

        btnEnroll.addActionListener(e -> {

            String result = courseService.enrollStudent(
                    txtEnrollCourseId.getText(),
                    txtEnrollStudentId.getText()
            );

            if ("OK".equals(result)) {

                statusLabel.setText("✅ Enrollment successful!");

                loadEnrollTable(
                        txtEnrollCourseId.getText().trim());

                loadCourseTable();

            } else {

                statusLabel.setText("⚠️ " + result);
                showMsg("⚠️ " + result);
            }
        });

        btnUnenroll.addActionListener(e -> {

            String courseId =
                    txtEnrollCourseId.getText().trim();

            String studentId =
                    txtEnrollStudentId.getText().trim();

            if (courseId.isBlank() || studentId.isBlank()) {

                showMsg(
                        "⚠️ Please enter both Course ID and Student ID!"
                );
                return;
            }

            courseService.unenrollStudent(
                    courseId,
                    studentId
            );

            statusLabel.setText(
                    "✅ Student unenrolled successfully!"
            );

            loadEnrollTable(courseId);

            loadCourseTable();
        });

        btnView.addActionListener(e ->
                loadEnrollTable(
                        txtEnrollCourseId.getText().trim()));

        return root;
    }

    private void loadEnrollTable(String courseId) {

        enrollModel.setRowCount(0);

        if (courseId.isBlank()) {
            return;
        }

        for (String[] row :
                courseService.getStudentsInCourse(courseId)) {

            enrollModel.addRow(row);
        }
    }

    // =========================================================
    // MY COURSES TAB
    // =========================================================

    private JPanel buildStudentCoursePanel() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10));

        JPanel top =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                5));

        txtStudentLookupId = new JTextField(15);

        JButton btnFind =
                new JButton("🔍 Find Courses");

        top.add(new JLabel("Student ID:"));
        top.add(txtStudentLookupId);
        top.add(btnFind);

        studentCourseModel =
                new DefaultTableModel(
                        new String[]{
                                "Course ID",
                                "Course Name",
                                "Capacity"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        studentCourseTable =
                new JTable(studentCourseModel);

        root.add(top, BorderLayout.NORTH);
        root.add(
                new JScrollPane(studentCourseTable),
                BorderLayout.CENTER);

        btnFind.addActionListener(e -> {

            String studentId =
                    txtStudentLookupId
                            .getText()
                            .trim();

            studentCourseModel.setRowCount(0);

            List<String[]> courses =
                    courseService.getCoursesOfStudent(studentId);

            if (courses.isEmpty()) {

                showMsg(
                        "No courses found for this student or the student does not exist."
                );

            } else {

                for (String[] row : courses) {

                    studentCourseModel.addRow(row);
                }
            }
        });

        return root;
    }

    // =========================================================
    // HELPER METHOD
    // =========================================================

    private void showMsg(String message) {

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }
}