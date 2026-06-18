package com.scrns.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:scrns.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}