package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQL implements UserDAO {

    public UserSQL() throws DataAccessException {
        DatabaseManager.initializeTables(USERS_TABLE_QUERY);
    }

    private static final String[] USERS_TABLE_QUERY = {
            "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "PRIMARY KEY (username)" +
                    ")"
    };


    @Override
    public UserData getUserWithUsername(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement =
                     conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String fetchedUsername = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    return new UserData(fetchedUsername, password, email);
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public UserData getUserWithEmail(String email) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement =
                     conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String fetchedEmail = resultSet.getString("email");
                    return new UserData(name, password, fetchedEmail);
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException {
        // Prevent any nulls from being inserted
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (email == null) {
            email = "";
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }



    @Override
    public void clearAllUsers() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement =
                     conn.prepareStatement("TRUNCATE TABLE users")) {
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }
}
