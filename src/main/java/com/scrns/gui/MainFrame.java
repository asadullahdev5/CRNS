package com.scrns.gui;

import com.scrns.model.Student;
import com.scrns.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextField txtId, txtName, txtSearch;
    private JTable table;
    private DefaultTableModel model;

    private StudentService service;

    public MainFrame() {

        setTitle("SCRNS - Ultimate System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        service = new StudentService();

        initUI();
        loadTable();

        setVisible(true);
    }

    private void initUI() {

        // ================= INPUT PANEL =================
        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));

        txtId = new JTextField();
        txtName = new JTextField();
        txtSearch = new JTextField();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");
        JButton btnLoad = new JButton("Load All");

        panel.add(new JLabel("ID"));
        panel.add(txtId);
        panel.add(btnSearch);

        panel.add(new JLabel("Name"));
        panel.add(txtName);
        panel.add(btnAdd);

        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnLoad);

        // ================= TABLE =================
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");

        table = new JTable(model);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= ROW CLICK =================
        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row != -1) {
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
            }
        });

        // ================= ADD =================
        btnAdd.addActionListener(e -> {

            Student s = new Student(txtId.getText(), txtName.getText());

            if (service.addStudent(s)) {
                showMsg("Student Added");
                loadTable();
            } else {
                showMsg("Fill all fields!");
            }
        });

        // ================= UPDATE =================
        btnUpdate.addActionListener(e -> {

            service.updateStudent(new Student(txtId.getText(), txtName.getText()));

            loadTable();
            showMsg("Updated");
        });

        // ================= DELETE =================
        btnDelete.addActionListener(e -> {

            service.deleteStudent(txtId.getText());

            loadTable();
            showMsg("Deleted");
        });

        // ================= SEARCH =================
        btnSearch.addActionListener(e -> {

            model.setRowCount(0);

            for (Student s : service.search(txtId.getText())) {

                model.addRow(new Object[]{
                        s.getId(),
                        s.getName()
                });
            }
        });

        // ================= LOAD =================
        btnLoad.addActionListener(e -> loadTable());
    }

    // ================= LOAD TABLE =================
    private void loadTable() {

        model.setRowCount(0);

        for (Student s : service.getAll()) {

            model.addRow(new Object[]{
                    s.getId(),
                    s.getName()
            });
        }
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}