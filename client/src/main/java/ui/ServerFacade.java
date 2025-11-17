package ui;

import com.google.gson.*;
import model.AuthData;
import model.GameData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String urlBeginning = "http://localhost:";
    private int portNumber = 8080;
    private Map<String, String> listGames = new HashMap<>();

    public ServerFacade(){
    }


    public String register(String username, String password, String email){
        try{
            JsonObject userData = new JsonObject();

            URL url = new URL(urlBeginning + portNumber + "/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            userData.addProperty("username", username);
            userData.addProperty("password", password);
            userData.addProperty("email", email);

            String userDataJson = new Gson().toJson(userData);

            conn.getOutputStream().write(userDataJson.getBytes());
            conn.connect();

            if (conn.getResponseCode() == 200){

                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                AuthData authToken = new Gson().fromJson(reader, AuthData.class);
                return authToken.getToken();
            }
            else{
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader).getAsJsonObject().get("message").getAsString();

                System.out.println(error);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public String login(String username, String password) {
        try {
            JsonObject userData = new JsonObject();

            URL url = new URL(urlBeginning + portNumber + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/json");
            userData.addProperty("username", username);
            userData.addProperty("password", password);
            String jsonData = new Gson().toJson(userData);

            conn.getOutputStream().write(jsonData.getBytes());
            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                AuthData authToken = new Gson().fromJson(reader, AuthData.class);
                return authToken.getToken();
            }
            else {
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader).getAsJsonObject().get("message").getAsString();

                System.out.println(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void logout(String token) {
        try {
            URL url = new URL(urlBeginning + portNumber + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "token");
            conn.connect();

            if (conn.getResponseCode() == 200) {
//                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                System.out.println("Logged out successfully");
//                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            }
            else {
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader).getAsJsonObject().get("message").getAsString();

                System.out.println(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String createGame(String gameName, String token) {
        try {
            URL url = new URL(urlBeginning + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("authorization", token);
            conn.setDoOutput(true);
            GameData currGame = new GameData(gameName);
            conn.setRequestProperty("Content-Type", "application/json");
            String jsonData = new Gson().toJson(currGame);

            conn.getOutputStream().write(jsonData.getBytes());
            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                GameData game = new Gson().fromJson(reader, GameData.class);
                System.out.println("Game created successfully");
                return game.getGameID();
            }
            else {
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader).getAsJsonObject().get("message").getAsString();

                System.out.println(error);
                return error;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean joinGame(String gameID, String playerColor, String token){
        try {
            GameData game = new GameData(playerColor, gameID);
            String gameName = null;

            URL url = new URL(urlBeginning + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("authorization", token);
            conn.setDoOutput(true);

            String jsonData = new Gson().toJson(game);
            conn.getOutputStream().write(jsonData.getBytes());
            conn.connect();

            if (conn.getResponseCode() == 200) {

                return true;
            }
            else{
                InputStreamReader reader = new InputStreamReader(conn.getErrorStream());
                String error = JsonParser.parseReader(reader).getAsJsonObject().get("message").getAsString();

                System.out.println(error);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public void listAllGames(String token){
        try {
            URL url = new URL(urlBeginning + portNumber + "/game");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", token);
            conn.setDoOutput(true);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder resp = new StringBuilder();
                while (reader.readLine() != null) {
                    resp.append(reader.readLine());
                }
                reader.close();
                System.out.println("Games:");
                JsonArray games = JsonParser.parseString(resp.toString()).getAsJsonObject().getAsJsonArray("games");
                if (games != null){
                    for (JsonElement game : games) {
                        String gameID = game.getAsJsonObject().get("gameID").getAsString();
                        String gameName = game.getAsJsonObject().get("gameName").getAsString();
                        JsonElement whiteElement = game.getAsJsonObject().get("whiteUsername");
                        String whiteUsername;
                        if (whiteElement != null) {
                            whiteUsername = whiteElement.getAsString();
                        } else {
                            whiteUsername = null;
                        }
                        JsonElement blackElement = game.getAsJsonObject().get("whiteUsername");
                        String blackUsername;
                        if (whiteElement != null) {
                            blackUsername = blackElement.getAsString();
                        } else {
                            blackUsername = null;
                        }
                        // Print the list of games
                        System.out.println("Games: ");
                        System.out.print("Game ID: " + gameID + ", Game name: " + gameName);
                        if (whiteUsername != null) {
                            System.out.print("White player: " + whiteUsername + ", ");
                        } else {
                            System.out.print("White player: empty" + ", ");
                        }
                        if (blackUsername != null) {
                            System.out.println("Black player: " + blackUsername + ", ");
                        } else {
                            System.out.println("Black player: empty");
                        }
                    }
                }
                else {
                    System.out.println("List of games is empty.");
                }
            } else {
                String error = conn.getResponseMessage();
                System.out.println("Error: " + error);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void clear() {
        try {
            URL url = new URL(urlBeginning + portNumber + "/db");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                System.out.println("DB cleared successfully!");
            } else {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getErrorStream());
                String errorMessage = JsonParser.parseReader(inputStreamReader).getAsJsonObject().get("message").getAsString();
                System.out.println("Error: " + errorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
