package org.raghavan.rec.java.project.pages;

import org.raghavan.rec.java.project.DatabaseConnection;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClassListScreen {

    private JTable classTable;
    private DefaultTableModel tableModel;

    public ClassListScreen() {
        // Create the frame (window) for ClassListScreen
        JFrame frame = new JFrame("Class List");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model to manage the class data
        String[] columnNames = {"Class ID", "Class Name", "Incharge Name", "Total Students"};
        tableModel = new DefaultTableModel(columnNames, 0);
        classTable = new JTable(tableModel);

        // Load class data from the database
        loadClassData();

        // Add the table to a JScrollPane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(classTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Class");
        JButton removeButton = new JButton("Remove Class");
        JButton editButton = new JButton("Edit Class");
        JButton backButton = new JButton("Back");
        JButton dashboardButton = new JButton("Dashboard");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(backButton);
        buttonPanel.add(dashboardButton);  // Add Dashboard button

        // Add button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for Add Class button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new class via input dialogs
                String className = JOptionPane.showInputDialog(frame, "Enter Class Name:");
                if (className != null && !className.trim().isEmpty()) {
                    String inchargeName = JOptionPane.showInputDialog(frame, "Enter Class Incharge Name:");
                    if (inchargeName != null && !inchargeName.trim().isEmpty()) {
                        String totalStudentsStr = JOptionPane.showInputDialog(frame, "Enter Total Students:");
                        if (totalStudentsStr != null && !totalStudentsStr.trim().isEmpty()) {
                            int totalStudents = Integer.parseInt(totalStudentsStr);
                            addClass(className, inchargeName, totalStudents);
                        }
                    }
                }
            }
        });

        // Action listener for Remove Class button
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = classTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int classId = (int) tableModel.getValueAt(selectedRow, 0);
                    removeClass(classId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a class to remove.");
                }
            }
        });

        // Action listener for Edit Class button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = classTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int classId = (int) tableModel.getValueAt(selectedRow, 0);
                    String className = (String) tableModel.getValueAt(selectedRow, 1);
                    String inchargeName = (String) tableModel.getValueAt(selectedRow, 2);
                    int totalStudents = (int) tableModel.getValueAt(selectedRow, 3);

                    // Create a panel with input fields for editing
                    JTextField classNameField = new JTextField(className);
                    JTextField inchargeNameField = new JTextField(inchargeName);
                    JTextField totalStudentsField = new JTextField(String.valueOf(totalStudents));

                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Class Name:"));
                    panel.add(classNameField);
                    panel.add(new JLabel("Incharge Name:"));
                    panel.add(inchargeNameField);
                    panel.add(new JLabel("Total Students:"));
                    panel.add(totalStudentsField);

                    int option = JOptionPane.showConfirmDialog(frame, panel, "Edit Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (option == JOptionPane.OK_OPTION) {
                        String newClassName = classNameField.getText();
                        String newInchargeName = inchargeNameField.getText();
                        int newTotalStudents = Integer.parseInt(totalStudentsField.getText());

                        updateClass(classId, newClassName, newInchargeName, newTotalStudents);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a class to edit.");
                }
            }
        });

        // Action listener for Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window (ClassListScreen) and go back to the previous screen
                frame.dispose();  // Close the current window (ClassListScreen)
                new Dashboard();  // Open the Dashboard window again
            }
        });

        // Action listener for Dashboard button
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window (ClassListScreen) and go to the Dashboard window
                frame.dispose();  // Close the current window (ClassListScreen)
                new Dashboard();  // Open the Dashboard window
            }
        });

        // Set the window to be centered on the screen
        frame.setLocationRelativeTo(null);

        // Make the window visible
        frame.setVisible(true);
    }

    // Load class data from the database
    private void loadClassData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ListOfClass")) {

            // Clear the table before adding new data
            tableModel.setRowCount(0);

            while (rs.next()) {
                int classId = rs.getInt("Class_id");
                String className = rs.getString("Class_name");
                String inchargeName = rs.getString("Class_inchargename");
                int totalStudents = rs.getInt("TotalStudent");
                tableModel.addRow(new Object[]{classId, className, inchargeName, totalStudents});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading class data: " + e.getMessage());
        }
    }

    // Add class to the database
    private void addClass(String className, String inchargeName, int totalStudents) {
        String query = "INSERT INTO ListOfClass (Class_name, Class_inchargename, TotalStudent) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, className);
            pstmt.setString(2, inchargeName);
            pstmt.setInt(3, totalStudents);
            pstmt.executeUpdate();

            // Reload the class data after insertion
            loadClassData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding class: " + e.getMessage());
        }
    }

    // Remove class from the database
    private void removeClass(int classId) {
        String query = "DELETE FROM ListOfClass WHERE Class_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, classId);
            pstmt.executeUpdate();

            // Reload the class data after deletion
            loadClassData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error removing class: " + e.getMessage());
        }
    }

    // Update class information in the database
    private void updateClass(int classId, String className, String inchargeName, int totalStudents) {
        String query = "UPDATE ListOfClass SET Class_name = ?, Class_inchargename = ?, TotalStudent = ? WHERE Class_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, className);
            pstmt.setString(2, inchargeName);
            pstmt.setInt(3, totalStudents);
            pstmt.setInt(4, classId);
            pstmt.executeUpdate();

            // Reload the class data after update
            loadClassData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating class: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ClassListScreen();
    }
}
