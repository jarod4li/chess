package dataaccess;


import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    void addGame(GameData newGame) throws DataAccessException;
    String getGameID(String gameName) throws DataAccessException;
    Boolean getGameName(String gameName) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
    Boolean setGame(GameData currGame, String playerColor, String username) throws DataAccessException;
    ArrayList<GameData> getList() throws DataAccessException;
    void clearAllGames() throws DataAccessException;
    //void updateGame(String gameID, GameData updatedGame) throws DataAccessException;
}

