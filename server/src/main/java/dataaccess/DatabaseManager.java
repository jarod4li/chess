package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
    public static void createDatabase() throws DataAccessException {
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
     */
    static Connection getConnection() throws DataAccessException {
        try {
            // do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    public static void initializeTables(String[] tableQueries) throws DataAccessException {
        createDatabase();
        try (Connection conn = getConnection()) {
            for (String query : tableQueries) {
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }


    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("db.properties")) {
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
            List<String> tables = getAllTables(conn);
            for (String table : tables) {
                printNullsInTable(conn, table);
            }
        } catch (Exception e) {
            System.out.println("DEBUG error: " + e.getMessage());
        }
    }

    private static List<String> getAllTables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }

        return tables;
    }

    private static void printNullsInTable(Connection conn, String table) throws SQLException {
        String query = "SELECT * FROM " + table;

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            var md = rs.getMetaData();
            int columnCount = md.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (rs.getString(i) == null) {
                        System.out.printf(
                                "⚠️ NULL FOUND in table '%s' column '%s'%n",
                                table,
                                md.getColumnName(i)
                        );
                    }
                }
            }
        }
    }
}
