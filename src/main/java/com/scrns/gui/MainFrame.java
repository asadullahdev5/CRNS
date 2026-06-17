package com.scrns.gui;

import com.scrns.model.*;
import com.scrns.notification.Notifiable;
import com.scrns.persistence.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextField txtId;
    private JTextField txtName;

    private JTable table;
    private DefaultTableModel model;

    private Course course;

    public MainFrame(){

        setTitle("SCRNS");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        course = new Course("OOP",3);

        initGUI();

        setVisible(true);
    }

    private void initGUI(){

        JPanel inputPanel = new JPanel(
                new GridLayout(3,2,10,10));

        txtId = new JTextField();
        txtName = new JTextField();

        JButton btnRegister =
                new JButton("Register Student");

        JButton btnSave =
                new JButton("Save");

        JButton btnLoad =
                new JButton("Load");

        inputPanel.add(new JLabel("Student ID"));
        inputPanel.add(txtId);

        inputPanel.add(new JLabel("Student Name"));
        inputPanel.add(txtName);

        inputPanel.add(btnRegister);
        inputPanel.add(btnSave);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Name");

        table = new JTable(model);

        add(inputPanel,BorderLayout.NORTH);
        add(new JScrollPane(table),
                BorderLayout.CENTER);

        add(btnLoad,BorderLayout.SOUTH);

        Notifiable notifier =
                msg -> JOptionPane.showMessageDialog(
                        this,msg);

        btnRegister.addActionListener(e -> {

            try{

                Student student =
                        new Student(
                                txtId.getText(),
                                txtName.getText());

                course.registerStudent(student);

                model.addRow(new Object[]{
                        txtId.getText(),
                        txtName.getText()
                });

                notifier.notifyUser(
                        "Registration Successful");

            }
            catch(CourseFullException ex){

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage()
                );
            }
        });

        btnSave.addActionListener(e -> {

            DataManager.saveCourse(course);

            JOptionPane.showMessageDialog(
                    this,
                    "Data Saved Successfully");
        });

        btnLoad.addActionListener(e -> {

            Course loaded =
                    DataManager.loadCourse();

            if(loaded!=null){

                course = loaded;

                model.setRowCount(0);

                for(Student s :
                        course.getStudents()){

                    model.addRow(new Object[]{
                            s.getRole(),
                            s.getName()
                    });
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Data Loaded Successfully");
            }
        });
    }
}