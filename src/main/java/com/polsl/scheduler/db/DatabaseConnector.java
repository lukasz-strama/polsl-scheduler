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

/**
 * Manages database connectivity and initialization for the PolSl Scheduler application.
 * <p>
 * This class is responsible for establishing connections to the SQLite database and ensuring
 * the database schema and initial data are correctly set up upon application startup.
 */
public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
    private static final String DB_URL = "jdbc:sqlite:scheduler.db";

    /**
     * Establishes a connection to the SQLite database file.
     *
     * @return A {@link Connection} object to the 'scheduler.db' SQLite database, or {@code null} if the connection fails.
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            logger.error("Failed to connect to database.", e);
            return null;
        }
    }

    /**
     * Initializes the database by executing the schema creation script.
     * <p>
     * This method handles both schema creation and data seeding. It first executes 'schema.sql' to ensure
     * tables exist. If the database is determined to be empty, it proceeds to execute 'seed.sql' to populate
     * it with initial sample data.
     * <p>
     * Uses try-with-resources to ensure the database connection is closed after initialization.
     */
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

    /**
     * Checks if the database contains any data by querying the 'subjects' table.
     * <p>
     * Uses try-with-resources to manage the Statement and ResultSet.
     *
     * @param conn The active database {@link Connection}.
     * @return {@code true} if the 'subjects' table has zero rows, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the table does not exist.
     */
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

    /**
     * Reads and executes SQL commands from a specified resource file.
     * <p>
     * The method reads the file from the classpath, splits content by the semicolon delimiter,
     * and executes each command individually. Uses try-with-resources to close the Scanner and Statement.
     *
     * @param conn     The active database {@link Connection}.
     * @param fileName The name of the SQL file located in the resources directory (e.g., "schema.sql").
     */
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