package ui;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private static final String URL_BEGINNING = "http://localhost:";
    private final int portNumber;
    private final Gson gson = new Gson();

    public ServerFacade(int portNumber) {
        this.portNumber = portNumber;
    }

    private String sendAuthRequest(String endpoint, UserData user) throws IOException {
        URL url = new URL(URL_BEGINNING + portNumber + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String jsonData = gson.toJson(user);
        conn.getOutputStream().write(jsonData.getBytes());
        conn.connect();

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                AuthData authToken = gson.fromJson(reader, AuthData.class);
                return authToken.getToken();
            }
        } else {
            try (InputStreamReader reader = new InputStreamReader(conn.getErrorStream())) {
                String error = JsonParser.parseReader(reader)
                        .getAsJsonObject()
                        .get("message")
                        .getAsString();
                System.out.println(error);
            }
            return null;
        }
    }

    public String register(String username, String password, String email) {
        try {
            UserData user = new UserData(username, password, email);
            return sendAuthRequest("/user", user);
        } catch (IOException e) {
            return null;
        }
    }

    public String login(String username, String password) {
        try {
            UserData user = new UserData(username, password, null);
            return sendAuthRequest("/session", user);
        } catch (IOException e) {
            return null;
        }
    }

    public void logout(String token) {
        try {
            URL url = new URL(URL_BEGINNING + portNumber + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("authorization", token);
            conn.connect();
        } catch (IOException ignored) {
        }
    }

    public String createGame(String gameName, String token) {
        try {
            URL url = new URL(URL_BEGINNING + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("authorization", token);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            GameData currGame = new GameData(gameName);
            String jsonData = gson.toJson(currGame);

            conn.getOutputStream().write(jsonData.getBytes());
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                var jsonObj = JsonParser.parseReader(reader).getAsJsonObject();
                String gameID = jsonObj.get("gameID").getAsString();
                System.out.println("Game created successfully");
                return gameID;
            } else {
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader)
                        .getAsJsonObject()
                        .get("message")
                        .getAsString();

                System.out.println(error);
                return error;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public boolean joinGame(String gameID, String playerColor, String token) {
        try {
            GameData game = new GameData(playerColor, gameID);

            URL url = new URL(URL_BEGINNING + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("authorization", token);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonData = gson.toJson(game);
            conn.getOutputStream().write(jsonData.getBytes());
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader)
                        .getAsJsonObject()
                        .get("message")
                        .getAsString();
                System.out.println(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public GameData[] listAllGames(String token) {
        try {
            URL url = new URL(URL_BEGINNING + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", token);
            conn.setDoOutput(true);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                record ListGames(GameData[] games) {}
                ListGames games = gson.fromJson(reader, ListGames.class);
                return games.games();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        try {
            URL url = new URL(URL_BEGINNING + portNumber + "/db");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("DB cleared successfully!");
            } else {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getErrorStream());
                String errorMessage = JsonParser.parseReader(inputStreamReader)
                        .getAsJsonObject()
                        .get("message")
                        .getAsString();
                System.out.println("Error: " + errorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
