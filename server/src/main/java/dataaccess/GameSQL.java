package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameSQL implements GameDAO{
    private final Gson gson;
    public GameSQL() throws DataAccessException{
        gson = new Gson();
        configureDatabase();
    }
    private static final String[] GAMES_TABLE_QUERY = {
            "CREATE TABLE IF NOT EXISTS games (" +
                    "gameName VARCHAR(255) NOT NULL, " +
                    "gameID VARCHAR(255) PRIMARY KEY, " +
                    "whiteUsername VARCHAR(255), " +
                    "blackUsername VARCHAR(255), " +
                    "game TEXT NOT NULL" +
                    ")"
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : GAMES_TABLE_QUERY) {
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
    private int count = 1;
    @Override
    public void addGame(GameData newGame) throws DataAccessException {
        String gameName = newGame.getName() == null ? "" : newGame.getName();
        String gameID = String.valueOf(count);
        String white = newGame.getWhite() == null ? "" : newGame.getWhite();
        String black = newGame.getBlack() == null ? "" : newGame.getBlack();

        if (newGame.getGame() == null) {
            newGame.setGame();
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO games (gameName, gameID, whiteUsername, blackUsername, game) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, gameName);
            statement.setString(2, gameID);
            statement.setString(3, white); // empty string instead of null
            statement.setString(4, black); // empty string instead of null
            statement.setString(5, serializeGame(newGame.getGame()));

            statement.executeUpdate();
            count++;

        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }



    @Override
    public String getGameID(String gameName) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM games WHERE gameName=?")) {
            statement.setString(1, gameName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("gameID");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return null;
    }
    @Override
    public Boolean getGameName(String gameName) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM games WHERE gameName=?")) {
            statement.setString(1, gameName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String gameID = resultSet.getString("gameID");
                    return gameID != null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
        return false;
    }
    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT gameName, whiteUsername, blackUsername, game FROM games WHERE gameID = ?")) {
            statement.setString(1, gameID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String gameName = rs.getString("gameName");
                    String white    = rs.getString("whiteUsername");
                    String black    = rs.getString("blackUsername");
                    String gameJson = rs.getString("game");

                    // Convert empty strings back to null for API output
                    white = (white == null || white.isEmpty()) ? null : white;
                    black = (black == null || black.isEmpty()) ? null : black;

                    return new GameData(gameName, gameID, white, black, deserializeGame(gameJson));
                }
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public Boolean setGame(GameData currGame, String playerColor, String username) throws DataAccessException {
        GameData newGame = getGame(currGame.getGameID());
        if (newGame == null) {
            return false;
        }

        if (playerColor == null) {
            return true;
        }

        if (playerColor.equals("WHITE") && (newGame.getWhite() == null || newGame.getWhite().isEmpty())) {
            newGame.setWhite(username);
        } else if (playerColor.equals("BLACK") && (newGame.getBlack() == null || newGame.getBlack().isEmpty())) {
            newGame.setBlack(username);
        } else {
            return false; // color already taken
        }

        // Delete the old game entry before inserting the updated one
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM games WHERE gameID = ?")) {
            statement.setString(1, newGame.getGameID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting game: " + e.getMessage());
        }

        // Re-insert the updated game (ensure no nulls)
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO games (gameName, gameID, whiteUsername, blackUsername, game) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, newGame.getName() == null ? "" : newGame.getName());
            statement.setString(2, newGame.getGameID());
            statement.setString(3, newGame.getWhite() == null ? "" : newGame.getWhite());
            statement.setString(4, newGame.getBlack() == null ? "" : newGame.getBlack());
            statement.setString(5, serializeGame(newGame.getGame()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting updated game: " + e.getMessage());
        }

        return true;
    }

    @Override
    public ArrayList<GameData> getList() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT gameName, gameID, whiteUsername, blackUsername, game FROM games")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String gameName = rs.getString("gameName");
                    String gameID   = rs.getString("gameID");
                    String white    = rs.getString("whiteUsername");
                    String black    = rs.getString("blackUsername");
                    String gameJson = rs.getString("game");

                    // Convert empty strings back to null for API output
                    white = (white == null || white.isEmpty()) ? null : white;
                    black = (black == null || black.isEmpty()) ? null : black;

                    games.add(new GameData(gameName, gameID, white, black, deserializeGame(gameJson)));
                }
                return games;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void clearAllGames() throws DataAccessException{
        count = 1;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement("TRUNCATE TABLE games")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
    private String serializeGame(ChessGame game) {
        return gson.toJson(game);
    }
    private ChessGame deserializeGame(String json) {
        return gson.fromJson(json, ChessGame.class);
    }

    @Override
    public void updateGame(String gameID, GameData updatedGameData) throws DataAccessException {
        String whiteUser = updatedGameData.getWhite() == null ? "" : updatedGameData.getWhite();
        String blackUser = updatedGameData.getBlack() == null ? "" : updatedGameData.getBlack();
        String gameName  = updatedGameData.getName()  == null ? "" : updatedGameData.getName();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {
            statement.setString(1, whiteUser);
            statement.setString(2, blackUser);
            statement.setString(3, gameName);
            statement.setString(4, serializeGame(updatedGameData.getGame()));
            statement.setString(5, gameID);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: internal server error");
        }
    }

}