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

        JButton dashboardButton = new JButton("Dashboard");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(dashboardButton);

        // Add button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for Add Class button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a panel with fields for class input
                JPanel panel = new JPanel(new GridLayout(0, 1));
                JTextField classNameField = new JTextField();
                JComboBox<String> inchargeDropdown = new JComboBox<>(getInchargeNames()); // Dropdown for incharge names
                JTextField totalStudentsField = new JTextField();

                // Add fields to the panel
                panel.add(new JLabel("Class Name:"));
                panel.add(classNameField);
                panel.add(new JLabel("Class Incharge:"));
                panel.add(inchargeDropdown);
                panel.add(new JLabel("Total Students:"));
                panel.add(totalStudentsField);

                // Show the dialog
                int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    // Fetch and validate inputs
                    String className = classNameField.getText().trim();
                    String inchargeName = (String) inchargeDropdown.getSelectedItem();
                    String totalStudentsStr = totalStudentsField.getText().trim();

                    if (!className.isEmpty() && !totalStudentsStr.isEmpty()) {
                        try {
                            int totalStudents = Integer.parseInt(totalStudentsStr);
                            addClass(className, inchargeName, totalStudents); // Add class to database
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Total Students must be a valid number.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "All fields are required.");
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

                    // Create input fields
                    JTextField classNameField = new JTextField(className);
                    JComboBox<String> inchargeDropdown = new JComboBox<>(getInchargeNames());
                    JTextField totalStudentsField = new JTextField(String.valueOf(totalStudents));

                    // Pre-select the current incharge name in the dropdown
                    inchargeDropdown.setSelectedItem(inchargeName);

                    // Create panel and add input fields
                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Class Name:"));
                    panel.add(classNameField);
                    panel.add(new JLabel("Incharge Name:"));
                    panel.add(inchargeDropdown);
                    panel.add(new JLabel("Total Students:"));
                    panel.add(totalStudentsField);

                    int option = JOptionPane.showConfirmDialog(frame, panel, "Edit Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            String newClassName = classNameField.getText().trim();
                            String newInchargeName = (String) inchargeDropdown.getSelectedItem();
                            int newTotalStudents = Integer.parseInt(totalStudentsField.getText().trim());

                            if (!newClassName.isEmpty() && newInchargeName != null) {
                                updateClass(classId, newClassName, newInchargeName, newTotalStudents);
                            } else {
                                JOptionPane.showMessageDialog(frame, "All fields are required.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Total Students must be a valid number.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a class to edit.");
                }
            }
        });


        // Action listener for Dashboard button
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

            pstmt.setString(1, className); // Set the class name
            pstmt.setString(2, inchargeName); // Set the selected incharge name from the dropdown
            pstmt.setInt(3, totalStudents); // Set the total students count
            pstmt.executeUpdate();

            // Reload the class data after insertion to reflect changes in the table
            loadClassData();

            // Confirmation message
            JOptionPane.showMessageDialog(null, "Class added successfully!");

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

    // Fetch distinct incharge names from the database
    private String[] getInchargeNames() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT name FROM professors")) {

            java.util.List<String> inchargeList = new java.util.ArrayList<>();
            while (rs.next()) {
                inchargeList.add(rs.getString("name"));
            }
            return inchargeList.toArray(new String[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching incharge names: " + e.getMessage());
            return new String[0];
        }
    }
}
