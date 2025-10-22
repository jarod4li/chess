package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.UserData;

import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final Gson gson = new Gson();

    private UserDAO user;
    private AuthDAO auth;
    private GameDAO game;
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    public Server() {


        userService = new UserService(user, auth);
        //gameService = new GameService(game, auth);
        //clearService = new ClearService(user, game, auth);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.post("/game", this::createGameHandler);
        javalin.put("/game", this::joinGameHandler);
        javalin.get("/game", this::listGamesHandler);
        javalin.delete("/db", this::clearApplicationHandler);
        javalin.delete("/session", this::logoutHandler);

        javalin.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json(Map.of("message", "Internal server error"));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void registerHandler(Context ctx) {
        try {
            UserData request = gson.fromJson(ctx.body(), UserData.class);
            AuthData authToken = userService.register(request);
            ctx.status(200).json(authToken);
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void loginHandler(Context ctx) {
        try {
            UserData request = gson.fromJson(ctx.body(), UserData.class);
            AuthData authToken = userService.logIn(request);
            ctx.status(200).json(authToken);
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }
    private void joinGameHandler(Context ctx) {
        try {
            String token = ctx.header("authorization");
            GameData request = gson.fromJson(ctx.body(), GameData.class);
            gameService.joinGame(request, token);
            ctx.status(200).result("{}");
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }
    private void createGameHandler(Context ctx) {
        try {
            String token = ctx.header("authorization");
            GameData request = gson.fromJson(ctx.body(), GameData.class);
            String gameID = gameService.registerGame(request, token);
            ctx.status(200).json(new GameData(null, gameID));
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void listGamesHandler(Context ctx) {
        try {
            String token = ctx.header("authorization");
            ArrayList<GameData> games = gameService.listGames(token);
            ctx.status(200).json(Map.of("games", games));
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void clearApplicationHandler(Context ctx) {
        try {
            clearService.clearApplication();
            ctx.status(200).result("{}");
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void logoutHandler(Context ctx) {
        try {
            String token = ctx.header("authorization");
            userService.logOut(token);
            ctx.status(200).result("{}");
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }



    private void handleError(Context ctx, DataAccessException e) {
        String message = e.getMessage();
        switch (message) {
            case "Error: bad request" -> ctx.status(400);
            case "Error: unauthorized" -> ctx.status(401);
            case "Error: already taken" -> ctx.status(403);
            default -> ctx.status(500);
        }
        ctx.json(Map.of("message", message));
    }
}
