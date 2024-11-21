package org.raghavan.rec.java.project.pages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard {

    // Constructor to set up the window
    public Dashboard() {
        // Create a JFrame (window)
        JFrame frame = new JFrame("Dashboard");

        // Set the size of the window
        frame.setSize(800, 600);

        // Set the default close operation (close the application when the window is closed)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label to display on the dashboard
        JLabel label = new JLabel("Welcome to the Dashboard", JLabel.CENTER);
        frame.add(label);

        // Create a button to navigate to the new page
        JButton goToRoomListButton = new JButton("Go to Room List");
        goToRoomListButton.setBounds(350, 250, 150, 40);  // Set position and size of the button
        frame.add(goToRoomListButton);  // Add the button to the frame

        // Action listener for the button
        goToRoomListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide the current Dashboard window
                frame.setVisible(false);

                // Open the new RoomListScreen window
                new RoomListScreen();  // Open the RoomListScreen (make sure RoomListScreen is another JFrame or a window)
            }
        });
// Create a Go to Class List button
        JButton classListButton = new JButton("Go to Class List");
        classListButton.setBounds(550, 250, 150, 40);
        //frame.add(classListButton, "Center");
        frame.add(classListButton);
        // Action listener for Go to Class List button
        classListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current window (Dashboard)
                frame.dispose();

                // Open the Class List screen
                new ClassListScreen();  // Create and show the Class List screen
            }
        });
        // Set layout to null to manage positioning manually
        frame.setLayout(null);

        // Set the window to be centered on the screen
        frame.setLocationRelativeTo(null);

        // Make the window visible
        frame.setVisible(true);
    }


}
