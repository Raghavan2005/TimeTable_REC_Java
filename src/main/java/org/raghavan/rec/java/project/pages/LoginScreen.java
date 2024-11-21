package org.raghavan.rec.java.project.pages;

import org.raghavan.rec.java.project.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame {

    // Constructor for the login screen
    public LoginScreen() {
        // Set title, size, and close operation
        setTitle("Login Screen");
        setSize(400, 400); // Increased the size to accommodate the image
        setLocationRelativeTo(null);  // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the dark color theme for the window
        getContentPane().setBackground(new Color(44, 44, 44)); // Slightly lighter dark background

        // Load and add the image at the top
        ImageIcon logoIcon = new ImageIcon("src/main/resources/logo.png");
        Image image = logoIcon.getImage(); // transform it
        Image newImage = image.getScaledInstance(300, 150, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        logoIcon = new ImageIcon(newImage); // transform it back
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        // Create a panel to hold the form elements
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for better control
        panel.setBackground(new Color(44, 44, 44)); // Slightly lighter dark background

        // Set up the labels and text fields with a light theme
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 5, 5); // Add padding between components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Align labels to the right

        JLabel userLabel = new JLabel("Username: ");
        userLabel.setForeground(Color.WHITE); // Light text color
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align text fields to the left
        JTextField usernameField = new JTextField(15);
        usernameField.setBackground(new Color(60, 60, 60)); // Dark text field background
        usernameField.setForeground(Color.WHITE); // Light text color
        usernameField.setCaretColor(Color.WHITE); // Set cursor color to white
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border to text field
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;

        JLabel passLabel = new JLabel("Password: ");
        passLabel.setForeground(Color.WHITE); // Light text color
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(60, 60, 60)); // Dark text field background
        passwordField.setForeground(Color.WHITE); // Light text color
        passwordField.setCaretColor(Color.WHITE); // Set cursor color to white
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border to text field
        panel.add(passwordField, gbc);

        // Create the login button with a modern design
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 70, 70)); // Slightly lighter button background
        loginButton.setForeground(Color.WHITE); // Light text on the button
        loginButton.setFocusPainted(false); // Remove focus border
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // Button text font
        loginButton.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border to button

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validate login credentials against the database
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    panel.setVisible(false); // Hide the main window
                    new Dashboard();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(loginButton, gbc);

        // Add the panel to the center of the JFrame
        add(panel, BorderLayout.CENTER);
    }

    // Method to validate the login credentials against the database
    private boolean validateLogin(String username, String password) {
        // Establish a connection to the database using the DatabaseConnection class
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    // If a result is returned, the login is successful
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
