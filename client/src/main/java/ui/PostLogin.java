package ui;

import java.util.Scanner;

public class PostLogin {
    private final static Scanner scanner = new Scanner(System.in);
    private final static ServerFacade serverFacade = new ServerFacade();


    public static void postLogin(String token){
        String gameName;
        String gameID;
        String playerColor;

        System.out.println("\nOptions:");
        System.out.println("Create game");
        System.out.println("Join game");
        System.out.println("List games");
        System.out.println("Help");
        System.out.println("Logout");

        while (true){
            String input = scanner.nextLine().toLowerCase();
            switch (input) {
                case "create game" -> {
                    System.out.println("\nEnter game name:");
                    gameName = scanner.nextLine();
                    gameID = serverFacade.createGame(gameName, token);
                    if (gameID != null) {
                        postLogin(token);
                    }
                }
                case "join game" -> {
                    System.out.println("Enter game ID:");
                    gameID = scanner.nextLine();
                    System.out.println("Join white, black, or none?");
                    playerColor = scanner.next().toLowerCase();
                    if (playerColor.equals("none")) {
                        playerColor = null;
                    }
                    while (!(playerColor.equals("white") || playerColor.equals("black"))) {
                        System.out.println("Please choose white, black, or none.");
                        playerColor = scanner.next().toLowerCase();
                        if (playerColor.equals("none")) {
                            playerColor = null;
                            break;
                        }
                    }
                    boolean joined = serverFacade.joinGame(gameID, playerColor, token);
                    if (joined) {
                        GameConsole.runGame();
                    } else {
                        postLogin(token);
                    }
                }
                case "list games" -> {
                    serverFacade.listAllGames(token);
                    postLogin(token);
                }
                case "help" -> {
                    System.out.println("Type one of the options.");
                    postLogin(token);
                }
                case "logout" -> {
                    serverFacade.logout(token);
                    PreLogin.preLogin();
                }
                default -> System.out.println("Invalid input! ");
            }
        }


    }
}
