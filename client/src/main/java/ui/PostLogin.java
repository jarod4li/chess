package ui;

import chess.ChessGame;
import model.GameData;

import java.util.Scanner;

public class PostLogin {
    private final static Scanner scanner = new Scanner(System.in);
    private static ChessGame.TeamColor teamColor;
    private static ServerFacade serverFacade;

    public static void postLogin(String token, Integer portNumber, ServerFacade serverFacade) {
    }

    public static ServerFacade getServerFacade() {
        return null;
    }
}
