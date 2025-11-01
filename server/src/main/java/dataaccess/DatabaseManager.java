package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }

    // TEMP DEBUG: find null cells in database
    public static void debugPrintNulls() {
        try (Connection conn = getConnection()) {
            var sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()";
            try (var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var table = rs.getString(1);
                    try (var stmt2 = conn.prepareStatement("SELECT * FROM " + table);
                         var rs2 = stmt2.executeQuery()) {
                        var md = rs2.getMetaData();
                        while (rs2.next()) {
                            for (int i = 1; i <= md.getColumnCount(); i++) {
                                if (rs2.getString(i) == null) {
                                    System.out.printf("⚠️ NULL FOUND in table '%s' column '%s'%n",
                                            table, md.getColumnName(i));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("DEBUG error: " + e.getMessage());
        }
    }

}
