package ui;

import java.util.Scanner;

public class PreLogin {
    private static Scanner scanner = new Scanner(System.in);
    private static ServerFacade facade = new ServerFacade();

    public static void preLogin(){
        String username;
        String password;
        String email;
        String token;

        System.out.println("Chess Game CS240");
        while(true){
            System.out.println("Choose one of the options:");
            System.out.println("Register");
            System.out.println("Login");
            System.out.println("Help");
            System.out.println("Quit");

            String input = scanner.nextLine().toLowerCase().trim();
            switch (input){
                case "register":
                    System.out.print("Username: ");
                    username = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    password = scanner.nextLine().trim();
                    System.out.print("email: ");
                    email = scanner.nextLine().trim();

                    token = facade.register(username, password, email);

                    if (token != null){
                        System.out.println("\nRegistration success! Welcome " + username);
                        PostLogin.postLogin(token);
                        break;
                    }

                case "login":
                    System.out.print("Username: ");
                    username = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    password = scanner.nextLine().trim();

                    token = facade.login(username, password);

                    if (token != null){
                        System.out.println("\nLogin success! Welcome " + username);
                    }
                    else{
                        System.out.println("No success :(");
                    }
                    PostLogin.postLogin(token);

                    break;
                case "help":
                    System.out.println("Select one of the given options and provide the requested information.");
                    break;
                case "quit":
                    System.out.println("Already leaving? Okay :*");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
