package com.polsl.scheduler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnector {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
    private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            logger.info("Successfully connected to SQLite database."); 
            
        } catch (SQLException e) {
            logger.error("Failed to connect to database: ", e); 
        }
        return conn;
    }
}