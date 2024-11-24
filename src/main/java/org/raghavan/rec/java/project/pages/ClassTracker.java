package org.raghavan.rec.java.project.pages;

import org.raghavan.rec.java.project.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.raghavan.rec.java.project.pages.Rooms;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class ClassTracker extends JFrame {
    ArrayList<String> cap = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable timetableTable;
    private JComboBox<String> roomDropdown;
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] columns = {"Day", "Period 1", "Period 2", "Period 3", "Period 4", "Period 5", "Period 6", "Period 7", "Period 8"};
   JPanel infoPanel;
    JLabel classNameLabel,classSizeLabel,smartboardLabel,speakerLabel;

    public ClassTracker() {
        // Set up the JFrame
        setTitle("Room Timetable Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create components
        JLabel titlelb = new JLabel("Select The Class Room", SwingConstants.CENTER);

        // Dropdown for rooms
        roomDropdown = new JComboBox<>(getRoomList());
        roomDropdown.addActionListener(e -> {
            if (roomDropdown.getItemCount() > 0) {
                updateRoomDetails(roomDropdown.getSelectedItem().toString());  // Update details on selection
                loadTimetable(roomDropdown.getSelectedItem().toString());
            }
        });

        // Add components for class name, class size, smartboard, and speaker
        infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Arrange components in a row
        classNameLabel = new JLabel("Class Name: ");
        classSizeLabel = new JLabel("Class Size: ");
        smartboardLabel = new JLabel("Smartboard Available: ");
        speakerLabel = new JLabel("Speaker Available: ");
        JButton Exportbtn = new JButton("Export as PDF");
        JButton dashboardButton = new JButton("Dashboard");
        // Add the components to the info panel
        infoPanel.add(classNameLabel);
        infoPanel.add(classSizeLabel);
        infoPanel.add(smartboardLabel);
        infoPanel.add(speakerLabel);


        // Table for timetable
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable direct cell editing
            }

            @Override
            public String getColumnName(int column) {
                return columns[column]; // Set column names
            }
        };

        // Add rows for days (first column)
        for (String day : days) {
            tableModel.addRow(new Object[]{day, "", "", "", "", "", "", "", ""});
        }

        timetableTable = new JTable(tableModel);
        timetableTable.setRowHeight(70);

        // Combine title, dropdown, and information panel into the top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1)); // 3 rows: title, dropdown, info panel
        topPanel.add(titlelb);
        topPanel.add(roomDropdown);
        topPanel.add(infoPanel);

        // Combine title and table into a single panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(timetableTable), BorderLayout.CENTER);

        // Add components to the JFrame
        add(topPanel, BorderLayout.PAGE_START); // Add label, dropdown, and info at the top
        add(centerPanel, BorderLayout.CENTER);

        // Add action listener for cell clicks
        timetableTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Check for double-click
                    int row = timetableTable.rowAtPoint(evt.getPoint());
                    int col = timetableTable.columnAtPoint(evt.getPoint());

                    // Prevent popup on the first column
                    if (col == 0) {
                        return;
                    }

                    if (row >= 0 && col > 0) {
                        showPopup(row, col); // Show popup only on double-click
                    }
                }
            }
        });

        // Load the initial timetable and room details
        if (roomDropdown.getItemCount() > 0) {
            updateRoomDetails(roomDropdown.getSelectedItem().toString());
            loadTimetable(roomDropdown.getSelectedItem().toString());
        }
TableModelToPDFExporter tp = new TableModelToPDFExporter();
        Exportbtn.addActionListener(e -> {
           tp.exportToPDF(tableModel,"D:\\time.pdf");
        });

infoPanel.add(Exportbtn);
        infoPanel.add(dashboardButton);

        dashboardButton.addActionListener(e -> {
            // Implement your dashboard action here
            // JOptionPane.showMessageDialog(ProfessorListScreen.this, "Navigating to Dashboard...");
            this.dispose();  // Close the current window (ClassListScreen)
            new Dashboard();
        });
        setVisible(true);
    }

    // Fetch list of rooms from the database
    private String[] getRoomList() {
        ArrayList<String> rooms = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT Room_Name FROM roomlist");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                rooms.add(resultSet.getString("Room_Name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms.isEmpty() ? new String[]{"No rooms available"} : rooms.toArray(new String[0]);
    }

    // Fetch room details from the database
    private void updateRoomDetails(String roomName) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Room_Capacity, Smartboard, Spacker FROM roomlist WHERE Room_Name = ?")) {
            statement.setString(1, roomName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int capacity = resultSet.getInt("Room_Capacity");
                boolean smartboard = resultSet.getBoolean("Smartboard");
                boolean speaker = resultSet.getBoolean("Spacker");

                classNameLabel.setText("Class Name: " + roomName);
                classSizeLabel.setText("Class Size: " + capacity);
                smartboardLabel.setText("Smartboard Available: " + (smartboard ? "Yes" : "No"));
                speakerLabel.setText("Speaker Available: " + (speaker ? "Yes" : "No"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load timetable for the selected room
    private void loadTimetable(String room) {
        // Clear the table model before loading new data
        tableModel.setRowCount(0);

        // Repopulate rows with day names (first column)
        for (String day : days) {
            tableModel.addRow(new Object[]{day, "", "", "", "", "", "", "", ""});
        }

        // Load timetable data for the selected room
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM timetable WHERE room = ?")) {

            statement.setString(1, room);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int day = resultSet.getInt("day"); // 1-based index
                int period = resultSet.getInt("period"); // 1-based index
                String professor = resultSet.getString("professor");
                String className = resultSet.getString("class");

                // Update the appropriate cell in the table
                tableModel.setValueAt(professor + " \n " + className.toString().replace("No.Student :","").replace("Class Name :","")+"\n"+roomDropdown.getSelectedItem().toString(), day - 1, period);

                timetableTable.setDefaultRenderer(Object.class, new CustomCellRenderer());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Show popup for editing timetable cell
    private void showPopup(int row, int column) {
        String selectedRoom = roomDropdown.getSelectedItem().toString();

        JDialog dialog = new JDialog(this, "Edit Timetable Entry", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.anchor = GridBagConstraints.WEST;

        JLabel professorLabel = new JLabel("Professor:");
        JComboBox<String> professorDropdown = new JComboBox<>(getProfessorList());

        JLabel classLabel = new JLabel("Class:");
        JComboBox<String> classDropdown = new JComboBox<>(getClassList());


// Add an ActionListener for dynamic updates
        //System.out.println(cap);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JButton deleteButton = new JButton("Delete");


        Font font = new Font("Arial", Font.PLAIN, 14);
        professorLabel.setFont(font);
        professorDropdown.setFont(font);
        classLabel.setFont(font);
        classDropdown.setFont(font);
        okButton.setFont(font);
        cancelButton.setFont(font);
        deleteButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(professorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(professorDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(classLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(classDropdown, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
      //  dialog.add(sizeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);
        dialog.add(buttonPanel, gbc);

        okButton.addActionListener(e -> {
            String professor = professorDropdown.getSelectedItem().toString();
            String className = classDropdown.getSelectedItem().toString();
            updateTimetable(selectedRoom, row + 1, column, professor, className);
            tableModel.setValueAt(professor + " \n " + className.toString().replace("No.Student :","").replace("Class Name :","")+"\n"+roomDropdown.getSelectedItem().toString(), row, column);
          System.out.println(professor + " \n " + className.toString().replace("No.Student :","").replace("Class Name :","")+"\n"+roomDropdown.getSelectedItem().toString());
            loadTimetable(roomDropdown.getSelectedItem().toString());
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        deleteButton.addActionListener(e -> {
            deleteTimetableEntry(selectedRoom, row + 1, column);
            tableModel.setValueAt("Unallocated", row, column); // Clear the cell in the table
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void deleteTimetableEntry(String room, int day, int period) {
        String query = "DELETE FROM timetable WHERE room = ? AND day = ? AND period = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, room);
            statement.setInt(2, day);
            statement.setInt(3, period);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Fetch list of professors
    private String[] getProfessorList() {
        ArrayList<String> professorSubjects = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT professors.name AS professor_name, professor_subjects.subject_name " +
                             "FROM professors " +
                             "JOIN professor_subjects ON professors.id = professor_subjects.professor_id"
             );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String professorName = resultSet.getString("professor_name");
                String subjectName = resultSet.getString("subject_name");
                professorSubjects.add(professorName + " " + subjectName); // Format the entry
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return professorSubjects.toArray(new String[0]);
    }


    // Fetch list of classes
    private String[] getClassList() {
        ArrayList<String> classes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Class_name, TotalStudent FROM listofclass");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String className = resultSet.getString("Class_name");
                int classSize = resultSet.getInt("TotalStudent");
                classes.add("Class Name : "+className + " - No.Student : " + classSize); // Format the entry
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes.toArray(new String[0]);
    }





    // Update timetable in the database
    private void updateTimetable(String room, int day, int period, String professor, String className) {
        String query = "INSERT INTO timetable (room, day, period, professor, class) " +
                "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE professor = ?, class = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, room);
            statement.setInt(2, day);
            statement.setInt(3, period);
            statement.setString(4, professor);
            statement.setString(5, className);
            statement.setString(6, professor);
            statement.setString(7, className);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClassTracker::new);
    }
}
