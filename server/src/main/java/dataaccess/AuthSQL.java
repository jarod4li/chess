
package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSQL implements AuthDAO{
    public AuthSQL() throws DataAccessException {
        configureDatabase();
    }
    private static final String[] AUTH_TABLE_QUERY = {
            "CREATE TABLE IF NOT EXISTS authtokens (" +
                    "token VARCHAR(255) NOT NULL, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY (token)" +
                    ")"
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : AUTH_TABLE_QUERY) {
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
    @Override
    public AuthData addAuthToken(String username) throws DataAccessException {
        return null;
    }
    @Override
    public AuthData findToken(String token) throws DataAccessException{
        return null;
    }
    @Override
    public void removeAuthToken(AuthData token) throws DataAccessException{

    }
    @Override
    public void clearAllAuth() throws DataAccessException {
    }
}
