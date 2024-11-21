package org.raghavan.rec.java.project;

import org.raghavan.rec.java.project.pages.LoginScreen;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection connection = org.raghavan.rec.java.project.DatabaseConnection.getConnection();


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });



       // org.raghavan.rec.java.project.DatabaseConnection.closeConnection(connection);
    }
}