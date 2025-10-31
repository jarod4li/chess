package dataaccess;

import model.UserData;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSQL implements UserDAO {
    public GameSQL() throws DataAccessException  {
        configureDatabase();
    }
    private static final String[] USERS_TABLE_QUERY = {
            "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "PRIMARY KEY (username)" +
                    ")"
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : USERS_TABLE_QUERY) {
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public UserData getUserWithUsername(String username) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUserWithEmail(String email) throws DataAccessException {
        return null;
    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public void clearAllUsers() throws DataAccessException {

    }
}