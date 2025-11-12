package ui;

import com.google.gson.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String urlBeginning = "http://localhost:";
    private int portNumber;
    private Map<String, String> listGames = new HashMap<>();


    public ServerFacade(int portNumber) {
        this.portNumber = portNumber;
    }

    public String register(String username, String password, String email) {
        return null;
    }

    public String login(String username, String password) {
        return null;
    }

    public void logout(String token) {
    }

    public String createGame(String gameName, String token) {
        return null;
    }

    public boolean joinGame(String gameID, String playerColor, String token) {
        return false;
    }

    public GameData[] listAllGames(String token) {
        return null;
    }

    public void clear() {
    }
}
