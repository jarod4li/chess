package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ServerFacadeTests {

    // Test data
    private final String username = "Jared";
    private final String password = "1234";
    private final String email = "jared@gmail";   // add ".com" if your server validates emails strictly
    private final String gameName = "new game";

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);              // start HTTP server on an open port
        serverFacade = new ServerFacade(port); // adjust ctor if yours is different
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() {
        serverFacade.clear();
    }

    @Test
    public void testRegisterPositive() {
        String authToken = serverFacade.register(username, password, email);
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void testRegisterNegative() {
        String token = serverFacade.register(username, password, email);
        Assertions.assertNotNull(token);

        // Try to register same user again
        token = serverFacade.register(username, password, email);
        Assertions.assertNull(token);
    }

    @Test
    public void testLoginPositive() {
        String token = serverFacade.register(username, password, email);
        serverFacade.logout(token);

        token = serverFacade.login(username, password);
        Assertions.assertNotNull(token);
    }

    @Test
    public void testLoginNegative() {
        String authToken = serverFacade.login(username + "stuff", password);
        Assertions.assertNull(authToken);
    }

    @Test
    public void testLogoutPositive() {
        String token = serverFacade.register(username, password, email);
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(token));
    }

}
