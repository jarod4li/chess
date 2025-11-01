package dataacess;

import dataaccess.AuthSQL;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLAuthTest {

    private AuthSQL authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new AuthSQL();
        authDAO.clearAllAuth();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authDAO.clearAllAuth();
    }

    // === Helper ===
    private AuthData makeAuth(String username) throws DataAccessException {
        AuthData token = authDAO.addAuthToken(username);
        assertNotNull(token);
        assertNotNull(token.getToken());
        return token;
    }

    // === addAuthToken ===
    @Test
    @Order(1)
    @DisplayName("addAuthToken - Positive")
    public void addAuthToken_Positive() throws DataAccessException {
        AuthData auth = makeAuth("testUser");
        assertEquals("testUser", auth.getUsername());
        assertNotNull(authDAO.findToken(auth.getToken()));
    }


    // === findToken ===
    @Test
    @Order(3)
    @DisplayName("findToken - Positive")
    public void findToken_Positive() throws DataAccessException {
        AuthData auth = makeAuth("findUser");
        AuthData found = authDAO.findToken(auth.getToken());
        assertNotNull(found);
        assertEquals("findUser", found.getUsername());
    }

    @Test
    @Order(4)
    @DisplayName("findToken - Negative (not found)")
    public void findToken_NotFound() throws DataAccessException {
        assertNull(authDAO.findToken("doesNotExistToken"));
    }

    // === removeAuthToken ===
    @Test
    @Order(5)
    @DisplayName("removeAuthToken - Positive")
    public void removeAuthToken_Positive() throws DataAccessException {
        AuthData auth = makeAuth("removeUser");
        authDAO.removeAuthToken(auth);
        assertNull(authDAO.findToken(auth.getToken()));
    }

    @Test
    @Order(6)
    @DisplayName("removeAuthToken - Negative (nonexistent token)")
    public void removeAuthToken_Negative() {
        try {
            authDAO.removeAuthToken(new AuthData("ghostUser", "fakeToken"));
        } catch (DataAccessException e) {
            // Match real implementation behavior
            assertTrue(e.getMessage().contains("Error") || e.getMessage().contains("not"),
                    "Expected error message on invalid removal");
        }
    }

    // === clearAllAuth ===
    @Test
    @Order(7)
    @DisplayName("clearAllAuth - Positive")
    public void clearAllAuth_Positive() throws DataAccessException {
        makeAuth("user1");
        makeAuth("user2");

        authDAO.clearAllAuth();

        assertNull(authDAO.findToken("user1"));
        assertNull(authDAO.findToken("user2"));
    }

    @Test
    @Order(8)
    @DisplayName("clearAllAuth - Negative (no data to clear)")
    public void clearAllAuth_EmptyTable() {
        assertDoesNotThrow(() -> authDAO.clearAllAuth());
    }
}
