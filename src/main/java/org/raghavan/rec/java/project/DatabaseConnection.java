package org.raghavan.rec.java.project;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static com.mysql.cj.telemetry.TelemetryAttribute.DB_USER;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/timetable";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method to establish the database connection
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");


        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        return connection;
    }
    public static Connection getConection() throws SQLException {
        try {
            // Attempt to establish a connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Print error if the connection fails
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }



    // Method to close the database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }



}
