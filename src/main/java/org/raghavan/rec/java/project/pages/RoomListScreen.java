package org.raghavan.rec.java.project.pages;

import org.raghavan.rec.java.project.DatabaseConnection;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RoomListScreen {

    private JTable roomTable;
    private DefaultTableModel tableModel;

    public RoomListScreen() {
        // Create the frame (window) for RoomListScreen
        JFrame frame = new JFrame("Room List");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model to manage the room data
        String[] columnNames = {"Room ID", "Room Name", "Capacity", "Smartboard", "Spacker"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);

        // Load room data from the database
        loadRoomData();

        // Add the table to a JScrollPane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(roomTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Room");
        JButton removeButton = new JButton("Remove Room");
        JButton editButton = new JButton("Edit Room");
        JButton backButton = new JButton("Back");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(backButton);

        // Add button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for Add Room button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new room via input dialogs
                String roomName = JOptionPane.showInputDialog(frame, "Enter Room Name:");
                if (roomName != null && !roomName.trim().isEmpty()) {
                    String capacityStr = JOptionPane.showInputDialog(frame, "Enter Room Capacity:");
                    if (capacityStr != null && !capacityStr.trim().isEmpty()) {
                        int capacity = Integer.parseInt(capacityStr);
                        boolean hasSmartboard = JOptionPane.showConfirmDialog(frame, "Does the room have a Smartboard?", "Smartboard", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                        boolean hasSpacker = JOptionPane.showConfirmDialog(frame, "Does the room have a Spacker?", "Spacker", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                        addRoom(roomName, capacity, hasSmartboard, hasSpacker);
                    }
                }
            }
        });

        // Action listener for Remove Room button
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int roomId = (int) tableModel.getValueAt(selectedRow, 0);
                    removeRoom(roomId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a room to remove.");
                }
            }
        });

        // Action listener for Edit Room button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int roomId = (int) tableModel.getValueAt(selectedRow, 0);
                    String roomName = (String) tableModel.getValueAt(selectedRow, 1);
                    int capacity = (int) tableModel.getValueAt(selectedRow, 2);
                    boolean hasSmartboard = (boolean) tableModel.getValueAt(selectedRow, 3);
                    boolean hasSpacker = (boolean) tableModel.getValueAt(selectedRow, 4);

                    // Create a panel with input fields for editing
                    JTextField roomNameField = new JTextField(roomName);
                    JTextField capacityField = new JTextField(String.valueOf(capacity));
                    JCheckBox smartboardCheckBox = new JCheckBox("Smartboard", hasSmartboard);
                    JCheckBox spackerCheckBox = new JCheckBox("Spacker", hasSpacker);

                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Room Name:"));
                    panel.add(roomNameField);
                    panel.add(new JLabel("Room Capacity:"));
                    panel.add(capacityField);
                    panel.add(smartboardCheckBox);
                    panel.add(spackerCheckBox);

                    int option = JOptionPane.showConfirmDialog(frame, panel, "Edit Room", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (option == JOptionPane.OK_OPTION) {
                        String newRoomName = roomNameField.getText();
                        int newCapacity = Integer.parseInt(capacityField.getText());
                        boolean newSmartboard = smartboardCheckBox.isSelected();
                        boolean newSpacker = spackerCheckBox.isSelected();

                        updateRoom(roomId, newRoomName, newCapacity, newSmartboard, newSpacker);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a room to edit.");
                }
            }
        });

        // Action listener for Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window (RoomListScreen) and go back to the Dashboard
                frame.dispose();  // Close the current window (RoomListScreen)
                new Dashboard();  // Open the Dashboard window again
            }
        });

        // Set the window to be centered on the screen
        frame.setLocationRelativeTo(null);

        // Make the window visible
        frame.setVisible(true);
    }

    // Load room data from the database
    private void loadRoomData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM RoomList")) {

            // Clear the table before adding new data
            tableModel.setRowCount(0);

            while (rs.next()) {
                int roomId = rs.getInt("Room_id");
                String roomName = rs.getString("Room_Name");
                int capacity = rs.getInt("Room_Capacity");
                boolean hasSmartboard = rs.getBoolean("Smartboard");
                boolean hasSpacker = rs.getBoolean("Spacker");
                tableModel.addRow(new Object[]{roomId, roomName, capacity, hasSmartboard, hasSpacker});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading room data: " + e.getMessage());
        }
    }

    // Add room to the database
    private void addRoom(String roomName, int capacity, boolean hasSmartboard, boolean hasSpacker) {
        String query = "INSERT INTO RoomList (Room_Name, Room_Capacity, Smartboard, Spacker) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roomName);
            pstmt.setInt(2, capacity);
            pstmt.setBoolean(3, hasSmartboard);
            pstmt.setBoolean(4, hasSpacker);
            pstmt.executeUpdate();

            // Reload the room data after insertion
            loadRoomData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding room: " + e.getMessage());
        }
    }

    // Remove room from the database
    private void removeRoom(int roomId) {
        String query = "DELETE FROM RoomList WHERE Room_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();

            // Reload the room data after deletion
            loadRoomData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error removing room: " + e.getMessage());
        }
    }

    // Update room information in the database
    private void updateRoom(int roomId, String roomName, int capacity, boolean hasSmartboard, boolean hasSpacker) {
        String query = "UPDATE RoomList SET Room_Name = ?, Room_Capacity = ?, Smartboard = ?, Spacker = ? WHERE Room_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roomName);
            pstmt.setInt(2, capacity);
            pstmt.setBoolean(3, hasSmartboard);
            pstmt.setBoolean(4, hasSpacker);
            pstmt.setInt(5, roomId);
            pstmt.executeUpdate();

            // Reload the room data after update
            loadRoomData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating room: " + e.getMessage());
        }
    }


}
