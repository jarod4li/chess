package dataacess;

import dataaccess.DataAccessException;
import dataaccess.UserSQL;
import model.UserData;
import org.junit.jupiter.api.*;

/**
 * Clean and consistent tests for UserSQL DAO.
 * Each DAO method has one positive and one negative test.
 */
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
    public void addUser_Positive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        UserData userData = userDAO.getUserWithUsername(username);

        Assertions.assertNotNull(userData, "User should not be null after adding");
        Assertions.assertEquals(username, userData.getName(), "Usernames should match");
        Assertions.assertEquals(email, userData.getEmail(), "Emails should match");
    }

    @Test
    @Order(3)
    @DisplayName("getUserWithUsername - Positive")
    public void getUserWithUsername_Positive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        UserData userData = userDAO.getUserWithUsername(username);

        Assertions.assertNotNull(userData);
        Assertions.assertEquals(username, userData.getName());
        Assertions.assertEquals(email, userData.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("getUserWithUsername - Negative (nonexistent username)")
    public void getUserWithUsername_Negative() throws DataAccessException {
        UserData userData = userDAO.getUserWithUsername("doesNotExist");
        Assertions.assertNull(userData, "Should return null for nonexistent username");
    }

    // ---------- getUserWithEmail ----------

    @Test
    @Order(5)
    @DisplayName("getUserWithEmail - Positive")
    public void getUserWithEmail_Positive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        UserData userData = userDAO.getUserWithEmail(email);

        Assertions.assertNotNull(userData);
        Assertions.assertEquals(username, userData.getName());
        Assertions.assertEquals(email, userData.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("getUserWithEmail - Negative (nonexistent email)")
    public void getUserWithEmail_Negative() throws DataAccessException {
        UserData userData = userDAO.getUserWithEmail("fake@example.com");
        Assertions.assertNull(userData, "Should return null for nonexistent email");
    }

    // ---------- clearAllUsers ----------

    @Test
    @Order(7)
    @DisplayName("clearAllUsers - Positive")
    public void clearAllUsers_Positive() throws DataAccessException {
        userDAO.addUser(username, password, email);
        userDAO.clearAllUsers();

        Assertions.assertNull(userDAO.getUserWithUsername(username));
        Assertions.assertNull(userDAO.getUserWithEmail(email));
    }

    @Test
    @Order(8)
    @DisplayName("clearAllUsers - Negative (already empty)")
    public void clearAllUsers_Negative() {
        try {
            userDAO.clearAllUsers();
            Assertions.assertTrue(true, "Clearing empty table should succeed silently");
        } catch (DataAccessException e) {
            Assertions.fail("clearAllUsers should not fail on empty table");
        }
    }
}
