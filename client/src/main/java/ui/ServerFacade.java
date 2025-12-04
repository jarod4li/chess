package ui;

import com.google.gson.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private final String urlBeginning = "http://localhost:";
    private int portNumber;

    public ServerFacade(int portNumber){
        this.portNumber = portNumber;
    }

    public String register(String username, String password, String email){
        try{
            UserData user = new UserData(username, password, email);
            URL url = new URL(urlBeginning + portNumber + "/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.getOutputStream().write(new Gson().toJson(user).getBytes());
            conn.connect();

            if (conn.getResponseCode() == 200){
                return new Gson().fromJson(new InputStreamReader(conn.getInputStream()), AuthData.class).getToken();
            } else {
                System.out.println(JsonParser.parseReader(new InputStreamReader(conn.getErrorStream())).
                        getAsJsonObject().get("message").getAsString());
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }

    public String login(String username, String password) {
        try {
            UserData user = new UserData(username, password, null);
            URL url = new URL(urlBeginning + portNumber + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.getOutputStream().write(new Gson().toJson(user).getBytes());
            conn.connect();

            return conn.getResponseCode() == 200
                    ? new Gson().fromJson(new InputStreamReader(conn.getInputStream()), AuthData.class).getToken()
                    : null;
        } catch (Exception e) {
            return null;
        }
    }

    public void logout(String token) {
        try {
            URL url = new URL(urlBeginning + portNumber + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", token);
            conn.connect();
        } catch (Exception ignored) {}
    }

    public String createGame(String gameName, String token) {
        try {
            GameData game = new GameData(gameName);
            URL url = new URL(urlBeginning + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.getOutputStream().write(new Gson().toJson(game).getBytes());
            conn.connect();

            return conn.getResponseCode() == 200
                    ? new Gson().fromJson(new InputStreamReader(conn.getInputStream()), GameData.class).getGameID()
                    : null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean joinGame(String gameID, String playerColor, String token){ return false; }

    public model.GameData[] listAllGames(String token){ return null; }

    public void clear() { }
}
