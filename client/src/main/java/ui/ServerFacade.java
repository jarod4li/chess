package ui;

import com.google.gson.*;
import model.AuthData;
import model.UserData;

import java.io.IOException;
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

            String jsonData = new Gson().toJson(user);
            conn.getOutputStream().write(jsonData.getBytes());

            conn.connect();

            if (conn.getResponseCode() == 200){
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                AuthData authToken = new Gson().fromJson(reader, AuthData.class);
                return authToken.getToken();
            } else {
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader).getAsJsonObject().get("message").getAsString();
                System.out.println(error);
                return null;
            }

        } catch (IOException e){
            e.printStackTrace();
        }
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

    public boolean joinGame(String gameID, String playerColor, String token){
        return false;
    }

    public model.GameData[] listAllGames(String token){
        return null;
    }

    public void clear() {
    }
}
