package org.raghavan.rec.java.project.pages;

import org.raghavan.rec.java.project.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class ProfessorListScreen extends JFrame {

    private DefaultTableModel tableModel;
    private JTable professorTable;

    public ProfessorListScreen() {
        // Set up the JFrame
        setTitle("Professor Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize table and model
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Position", "Joining Date", "Class Incharge", "Subjects"}, 0);
        professorTable = new JTable(tableModel);

        // Load professor data into the table
        loadProfessorList();
        setLocationRelativeTo(null);
        // Add components to the frame
        add(new JScrollPane(professorTable), BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add New Professor");
        JButton deleteButton = new JButton("Delete Professor");
        JButton editButton = new JButton("Edit Professor");
        JButton dashboardButton = new JButton("Dashboard");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(dashboardButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> showAddProfessorDialog());
        deleteButton.addActionListener(e -> {
            int selectedRow = professorTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                if (deleteProfessor(id)) {
                    JOptionPane.showMessageDialog(ProfessorListScreen.this, "Professor deleted successfully!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(ProfessorListScreen.this, "Failed to delete professor.");
                }
            } else {
                JOptionPane.showMessageDialog(ProfessorListScreen.this, "Please select a professor to delete.");
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = professorTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                showEditProfessorDialog(id);
            } else {
                JOptionPane.showMessageDialog(ProfessorListScreen.this, "Please select a professor to edit.");
            }
        });

        dashboardButton.addActionListener(e -> {
            // Implement your dashboard action here
           // JOptionPane.showMessageDialog(ProfessorListScreen.this, "Navigating to Dashboard...");
            this.dispose();  // Close the current window (ClassListScreen)
            new Dashboard();
        });

        setVisible(true);
    }

    // Method to show the "Add Professor" dialog
    private void showAddProfessorDialog() {
        JDialog dialog = new JDialog(this, "Add New Professor", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));

        // Input fields
        JTextField nameField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField dateField = new JTextField();
        JCheckBox inchargeCheckBox = new JCheckBox();
        JTextField classField = new JTextField();
        JTextField subjectsField = new JTextField();

        // Add components to the dialog
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Position:"));
        dialog.add(positionField);
        dialog.add(new JLabel("Joining Date (YYYY-MM-DD):"));
        dialog.add(dateField);
        dialog.add(new JLabel("Class Incharge:"));
        dialog.add(inchargeCheckBox);
        dialog.add(new JLabel("Class Name:"));
        dialog.add(classField);
        dialog.add(new JLabel("Subjects (Comma-separated):"));
        dialog.add(subjectsField);
        setLocationRelativeTo(null);
        // Buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String position = positionField.getText().trim();
            String joiningDate = dateField.getText().trim();
            boolean isIncharge = inchargeCheckBox.isSelected();
            String className = classField.getText().trim();
            String subjects = subjectsField.getText().trim();

            if (!name.isEmpty() && !position.isEmpty() && !joiningDate.isEmpty() && !subjects.isEmpty()) {
                if (addProfessor(name, position, joiningDate, isIncharge, className, subjects)) {
                    JOptionPane.showMessageDialog(dialog, "Professor added successfully!");
                    refreshTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add professor.");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Fields cannot be empty.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Method to load professor data from the database into the table
    private void loadProfessorList() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT p.id, p.name, p.position, p.joining_date, p.is_class_incharge, p.class_name, " +
                             "GROUP_CONCAT(ps.subject_name) AS subjects " +
                             "FROM professors p LEFT JOIN professor_subjects ps ON p.id = ps.professor_id " +
                             "GROUP BY p.id");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                String joiningDate = resultSet.getString("joining_date");
                boolean isClassIncharge = resultSet.getBoolean("is_class_incharge");
                String className = resultSet.getString("class_name");
                String subjects = resultSet.getString("subjects");

                tableModel.addRow(new Object[]{
                        id,
                        name,
                        position,
                        joiningDate,
                        isClassIncharge ? "Yes (" + className + ")" : "No",
                        subjects
                });
            }

        } catch (Exception e) {
            System.out.println("Error loading professor list.");
            e.printStackTrace();
        }
    }

    // Method to refresh the table data
    private void refreshTable() {
        tableModel.setRowCount(0);
        loadProfessorList();
    }

    // Method to add a new professor to the database
    private boolean addProfessor(String name, String position, String joiningDate, boolean isIncharge, String className, String subjects) {
        String query = "INSERT INTO professors (name, position, joining_date, is_class_incharge, class_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, position);
            statement.setString(3, joiningDate);
            statement.setBoolean(4, isIncharge);
            statement.setString(5, isIncharge ? className : null);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    int professorId = keys.getInt(1);
                    addSubjects(professorId, subjects);
                }
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Error adding professor.");
            e.printStackTrace();
            return false;
        }
    }

    // Method to add subjects to a professor
    private void addSubjects(int professorId, String subjects) {
        String[] subjectArray = subjects.split(",");
        String query = "INSERT INTO professor_subjects (professor_id, subject_name) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (String subject : subjectArray) {
                statement.setInt(1, professorId);
                statement.setString(2, subject.trim());
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (Exception e) {
            System.out.println("Error adding subjects.");
            e.printStackTrace();
        }
    }

    // Method to delete a professor from the database
    private boolean deleteProfessor(int id) {
        String query = "DELETE FROM professors WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error deleting professor.");
            e.printStackTrace();
            return false;
        }
    }

    private void showEditProfessorDialog(int id) {
        // Retrieve the professor's current details
        Professor professor = getProfessorById(id);

        if (professor != null) {
            JDialog dialog = new JDialog(this, "Edit Professor", true);
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(7, 2, 10, 10));

            // Input fields for editing
            JTextField nameField = new JTextField(professor.getName());
            JTextField positionField = new JTextField(professor.getPosition());
            JTextField dateField = new JTextField(professor.getJoiningDate());
            JCheckBox inchargeCheckBox = new JCheckBox();
            inchargeCheckBox.setSelected(professor.isClassIncharge());
            JTextField classField = new JTextField(professor.getClassName());
            JTextField subjectsField = new JTextField(professor.getSubjects());

            // Add components to the dialog
            dialog.add(new JLabel("Name:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Position:"));
            dialog.add(positionField);
            dialog.add(new JLabel("Joining Date (YYYY-MM-DD):"));
            dialog.add(dateField);
            dialog.add(new JLabel("Class Incharge:"));
            dialog.add(inchargeCheckBox);
            dialog.add(new JLabel("Class Name:"));
            dialog.add(classField);
            dialog.add(new JLabel("Subjects (Comma-separated):"));
            dialog.add(subjectsField);

            // Buttons
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            dialog.add(saveButton);
            dialog.add(cancelButton);
dialog.setLocationRelativeTo(null);
            saveButton.addActionListener(e -> {
                String name = nameField.getText().trim();
                String position = positionField.getText().trim();
                String joiningDate = dateField.getText().trim();
                boolean isIncharge = inchargeCheckBox.isSelected();
                String className = classField.getText().trim();
                String subjects = subjectsField.getText().trim();

                if (!name.isEmpty() && !position.isEmpty() && !joiningDate.isEmpty() && !subjects.isEmpty()) {
                    if (updateProfessor(id, name, position, joiningDate, isIncharge, className, subjects)) {
                        JOptionPane.showMessageDialog(dialog, "Professor updated successfully!");
                        refreshTable();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to update professor.");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Fields cannot be empty.");
                }
            });

            cancelButton.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Professor not found.");
        }
    }


    private boolean updateProfessor(int id, String name, String position, String joiningDate, boolean isIncharge, String className, String subjects) {
        String query = "UPDATE professors SET name = ?, position = ?, joining_date = ?, is_class_incharge = ?, class_name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, position);
            statement.setString(3, joiningDate);
            statement.setBoolean(4, isIncharge);
            statement.setString(5, isIncharge ? className : null);
            statement.setInt(6, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                updateSubjects(id, subjects);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private Professor getProfessorById(int id) {
        String query = "SELECT p.id, p.name, p.position, p.joining_date, p.is_class_incharge, p.class_name, " +
                "GROUP_CONCAT(ps.subject_name) AS subjects " +
                "FROM professors p LEFT JOIN professor_subjects ps ON p.id = ps.professor_id " +
                "WHERE p.id = ? GROUP BY p.id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int professorId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String position = resultSet.getString("position");
                    String joiningDate = resultSet.getString("joining_date");
                    boolean isClassIncharge = resultSet.getBoolean("is_class_incharge");
                    String className = resultSet.getString("class_name");
                    String subjects = resultSet.getString("subjects");

                    return new Professor(professorId, name, position, joiningDate, isClassIncharge, className, subjects);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving professor by ID.");
            e.printStackTrace();
        }

        return null;
    }

    private void updateSubjects(int professorId, String subjects) {
        String deleteQuery = "DELETE FROM professor_subjects WHERE professor_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {

            deleteStmt.setInt(1, professorId);
            deleteStmt.executeUpdate();  // Delete existing subjects

            String[] subjectArray = subjects.split(",");
            String insertQuery = "INSERT INTO professor_subjects (professor_id, subject_name) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                for (String subject : subjectArray) {
                    insertStmt.setInt(1, professorId);
                    insertStmt.setString(2, subject.trim());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProfessorListScreen::new);
    }
}
