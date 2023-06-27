package com.example.projet;

import java.sql.*;

public class dataBaseConnection {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String DB_USERNAME = "postgres";
    static final String DB_PASSWORD = "123456789";

    private Connection conn;

    public dataBaseConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}