package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private final Javalin server;
    private final Gson serializer;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        serializer = new Gson();

        UserDAO userDAO = new UserDataDAO();
        AuthDAO authDAO = new AuthDataDAO();
        GameDAO gameDAO = new GameDataDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);

        server.delete("db", this::clearApplication);
        server.delete("session", this::logout);
        server.post("user", this::register);
        server.post("session", this::login);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
        server.get("game", this::listGames);
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }

    private void clearApplication(Context ctx) {
        try {
            clearService.clearApplication();
            ctx.status(HttpStatus.OK);
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void register(Context ctx) {
        try {
            var user = serializer.fromJson(ctx.body(), UserData.class);
            var auth = userService.register(user);
            ctx.status(HttpStatus.OK).result(serializer.toJson(auth));
        } catch (JsonSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .result(serializer.toJson(errorMessage("bad request")));
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void login(Context ctx) {
        try {
            // parse body (may throw JsonSyntaxException for malformed JSON)
            var user = serializer.fromJson(ctx.body(), UserData.class);

            // If JSON parsed but required fields are missing -> 400 Bad Request
            if (user == null || user.getUsername() == null || user.getPassword() == null) {
                ctx.status(HttpStatus.BAD_REQUEST)
                        .result(serializer.toJson(errorMessage("bad request")));
                return;
            }

            // normal login flow
            var auth = userService.logIn(user);
            ctx.status(HttpStatus.OK).result(serializer.toJson(auth));
        } catch (JsonSyntaxException e) {
            // malformed JSON -> 400 Bad Request
            ctx.status(HttpStatus.BAD_REQUEST)
                    .result(serializer.toJson(errorMessage("bad request")));
        } catch (DataAccessException e) {
            // service-level errors -> map to appropriate status/message
            handleError(ctx, e);
        }
    }


    private void logout(Context ctx) {
        try {
            String token = ctx.header("authorization");
            userService.logOut(token);
            ctx.status(HttpStatus.OK);
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void createGame(Context ctx) {
        try {
            String token = ctx.header("authorization");
            var request = serializer.fromJson(ctx.body(), GameData.class);
            String gameID = gameService.registerGame(request, token);
            ctx.status(HttpStatus.OK)
                    .result(serializer.toJson(Map.of("gameID", Integer.parseInt(gameID))));
        } catch (JsonSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .result(serializer.toJson(errorMessage("bad request")));
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void joinGame(Context ctx) {
        try {
            String token = ctx.header("authorization");
            var request = serializer.fromJson(ctx.body(), GameData.class);
            gameService.joinGame(request, token);
            ctx.status(HttpStatus.OK);
        } catch (JsonSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .result(serializer.toJson(errorMessage("bad request")));
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }

    private void listGames(Context ctx) {
        try {
            String token = ctx.header("authorization");
            ArrayList<GameData> games = gameService.listGames(token);
            ctx.status(HttpStatus.OK)
                    .result(serializer.toJson(Map.of("games", games)));
        } catch (DataAccessException e) {
            handleError(ctx, e);
        }
    }


    private void handleError(Context ctx, DataAccessException e) {
        String msg = e.getMessage();
        if (msg == null) msg = "Error: internal server error";

        switch (msg) {
            case "Error: bad request" -> ctx.status(HttpStatus.BAD_REQUEST);
            case "Error: unauthorized" -> ctx.status(HttpStatus.UNAUTHORIZED);
            case "Error: already taken" -> ctx.status(HttpStatus.FORBIDDEN);
            default -> ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ctx.result(serializer.toJson(errorMessage(msg.substring(7)))); // remove "Error: "
    }

    private Map<String, String> errorMessage(String message) {
        return Map.of("message", "Error: " + message);
    }
}
