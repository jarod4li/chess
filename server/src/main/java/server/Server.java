package server;

import com.google.gson.Gson;
import io.javalin.*;
import model.AuthData;
import model.UserData;


import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
//        javalin.post("/user", this::registerHandler);
//        javalin.post("/session", this::loginHandler);
//        javalin.post("/game", this::createGameHandler);
//        javalin.put("/game", this::joinGameHandler);
//        javalin.get("/game", this::listGamesHandler);
//       javalin.delete("/db", this::clearApplicationHandler);
//        javalin.delete("/session", this::logoutHandler);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

}
