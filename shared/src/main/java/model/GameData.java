package model;

import chess.ChessGame;
import java.util.Objects;

public class GameData {
    private final String gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private ChessGame game;
    //private String authToken;

    public GameData(String gameName, String gameID, String authToken) {
        this.gameID = gameID;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.gameName = gameName;
        this.game = null;
        //this.authToken = authToken;
    }

    public String getGameID() {
        return this.gameID;
    }

    public String getWhite() {
        return this.whiteUsername;
    }

    public String getBlack() {
        return this.blackUsername;
    }

    public void setWhite(String playerWhite) {
        this.whiteUsername = playerWhite;
    }

    public void setBlack(String playerBlack) {
        this.blackUsername = playerBlack;
    }

    public String getName() {
        return this.gameName;
    }

    public ChessGame getGame() {
        return this.game;
    }

//    public String getAuthToken() {
//        return this.authToken;
//    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GameData gameData = (GameData)o;
            return Objects.equals(this.gameName, gameData.gameName) && Objects.equals(this.gameID, gameData.gameID) && Objects.equals(this.whiteUsername, gameData.whiteUsername) && Objects.equals(this.blackUsername, gameData.blackUsername);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.gameName, this.gameID, this.whiteUsername, this.blackUsername});
    }

    public String toString() {
        return "GameData{gameName='" + this.gameName + "', gameID='" + this.gameID + "', whiteUsername='" + this.whiteUsername + "', blackUsername='" + this.blackUsername + "'}";
    }
}
