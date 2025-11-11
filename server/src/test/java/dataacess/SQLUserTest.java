package dataacess;

import dataaccess.DataAccessException;
import dataaccess.UserSQL;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLUserTest {

    private UserSQL userDAO;
    private final String username = "testUser";
    private final String password = "testPassword";
    private final String email = "test@example.com";

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new UserSQL();
        userDAO.clearAllUsers();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clearAllUsers();
    }

    // ---------- addUser ----------

    @Test
    @Order(1)
    @DisplayName("addUser - Positive")
    public void addUserPositive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        UserData userData = userDAO.getUserWithUsername(username);

        Assertions.assertNotNull(userData);
        Assertions.assertEquals(username, userData.getName());
        Assertions.assertEquals(email, userData.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("addUser - Negative (null username)")
    public void addUserNegative() {
        Assertions.assertThrows(DataAccessException.class, () ->
                userDAO.addUser(null, password, email));
    }

    // ---------- getUserWithUsername ----------

    @Test
    @Order(3)
    @DisplayName("getUserWithUsername - Positive")
    public void getUserWithUsernamePositive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        UserData userData = userDAO.getUserWithUsername(username);

        Assertions.assertNotNull(userData);
        Assertions.assertEquals(username, userData.getName());
    }

    @Test
    @Order(4)
    @DisplayName("getUserWithUsername - Negative (not found)")
    public void getUserWithUsernameNegative() throws DataAccessException {
        UserData userData = userDAO.getUserWithUsername("nonexistent");
        Assertions.assertNull(userData);
    }

    // ---------- getUserWithEmail ----------

    @Test
    @Order(5)
    @DisplayName("getUserWithEmail - Positive")
    public void getUserWithEmailPositive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        UserData userData = userDAO.getUserWithEmail(email);

        Assertions.assertNotNull(userData);
        Assertions.assertEquals(email, userData.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("getUserWithEmail - Negative (not found)")
    public void getUserWithEmailNegative() throws DataAccessException {
        UserData userData = userDAO.getUserWithEmail("fake@mail.com");
        Assertions.assertNull(userData);
    }

    // ---------- clearAllUsers ----------

    @Test
    @Order(7)
    @DisplayName("clearAllUsers - Positive")
    public void clearAllUsersPositive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        userDAO.clearAllUsers();

        Assertions.assertNull(userDAO.getUserWithUsername(username));
        Assertions.assertNull(userDAO.getUserWithEmail(email));
    }

    @Test
    @Order(8)
    @DisplayName("clearAllUsers - Negative (already empty)")
    public void clearAllUsersNegative() {
        Assertions.assertDoesNotThrow(() -> userDAO.clearAllUsers());
    }
}
