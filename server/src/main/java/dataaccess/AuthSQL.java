package dataaccess;

import model.AuthData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSQL implements AuthDAO {

    public AuthSQL() throws DataAccessException {
        DatabaseManager.initializeTables(AUTH_TABLE_QUERY);
    }

    private static final String[] AUTH_TABLE_QUERY = {
            "CREATE TABLE IF NOT EXISTS authtokens (" +
                    "token VARCHAR(255) NOT NULL, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY (token)" +
                    ")"
    };


    @Override
    public AuthData addAuthToken(String username) throws DataAccessException {
        AuthData newUser = new AuthData(username == null ? "" : username);
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO authtokens (username, token) VALUES (?, ?)")) {
            statement.setString(1, newUser.getUsername());
            statement.setString(2, newUser.getToken());
            statement.executeUpdate();
            return newUser;
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }


    @Override
    public AuthData findToken(String token) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement =
                     conn.prepareStatement("SELECT * FROM authtokens WHERE token = ?")) {
            statement.setString(1, token);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    return new AuthData(username, token);
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void removeAuthToken(AuthData token) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement =
                     conn.prepareStatement("DELETE FROM authtokens WHERE token = ?")) {
            statement.setString(1, token.getToken());
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void clearAllAuth() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement =
                     conn.prepareStatement("TRUNCATE TABLE authtokens")) {
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }
}
