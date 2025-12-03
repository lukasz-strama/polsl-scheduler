package com.polsl.scheduler.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
    private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            logger.error("Failed to connect to database.", e);
            return null;
        }
    }

    public static void initDatabase() {
        try (Connection conn = connect()) {
            if (conn == null) return;

            executeSqlScript(conn, "schema.sql");

            if (isDatabaseEmpty(conn)) {
                logger.info("Database is empty. Seeding sample data...");
                executeSqlScript(conn, "seed.sql");
            } else {
                logger.info("Database already contains data. Skipping seed.");
            }

        } catch (SQLException e) {
            logger.error("Database initialization failed", e);
        }
    }

    private static boolean isDatabaseEmpty(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM subjects";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false; 
    }

    private static void executeSqlScript(Connection conn, String fileName) {
        InputStream is = DatabaseConnector.class.getResourceAsStream("/" + fileName);
        if (is == null) {
            logger.error("File {} not found in resources!", fileName);
            return;
        }

        try (Scanner scanner = new Scanner(is);
             Statement statement = conn.createStatement()) {

            scanner.useDelimiter(";");

            int commands = 0;
            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (sql.isEmpty()) continue;
                
                statement.execute(sql);
                commands++;
            }
            logger.info("Executed {} commands from {}", commands, fileName);

        } catch (Exception e) {
            logger.error("Error executing script: " + fileName, e);
        }
    }
}