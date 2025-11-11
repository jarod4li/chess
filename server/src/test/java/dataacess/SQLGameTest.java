package dataacess;

import dataaccess.DataAccessException;
import dataaccess.GameSQL;
import model.GameData;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLGameTest {

    private GameSQL gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new GameSQL();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        gameDAO.clearAllGames();
    }

    // ---------- addGame ----------

    @Test
    @Order(1)
    @DisplayName("addGame - Positive")
    public void addGamePositive() throws DataAccessException {
        GameData testGame = new GameData("testGame");
        gameDAO.addGame(testGame);

        GameData retrieved = gameDAO.getGame("1");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("testGame", retrieved.getName());
    }

    @Test
    @Order(2)
    @DisplayName("addGame - Negative (null name)")
    public void addGameNegative() {
        Assertions.assertThrows(DataAccessException.class, () ->
                gameDAO.addGame(new GameData(null, null, null, null)));
    }

    // ---------- getGame ----------

    @Test
    @Order(3)
    @DisplayName("getGame - Positive")
    public void getGamePositive() throws DataAccessException {
        GameData testGame = new GameData("testGame");
        gameDAO.addGame(testGame);
        GameData retrieved = gameDAO.getGame("1");

        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("testGame", retrieved.getName());
    }

    @Test
    @Order(4)
    @DisplayName("getGame - Negative (not found)")
    public void getGameNegative() throws DataAccessException {
        GameData game = gameDAO.getGame("999");
        Assertions.assertNull(game);
    }

    // ---------- setGame ----------

    @Test
    @Order(5)
    @DisplayName("setGame - Positive")
    public void setGamePositive() throws DataAccessException {
        GameData game = new GameData("setGameTest");
        gameDAO.addGame(game);
        Assertions.assertTrue(gameDAO.setGame(game, "WHITE", "user1"));
    }

    @Test
    @Order(6)
    @DisplayName("setGame - Negative (already taken color)")
    public void setGameNegative() throws DataAccessException {
        GameData game = new GameData("occupiedGame", "1", "user1", null);
        gameDAO.addGame(game);
        Assertions.assertFalse(gameDAO.setGame(game, "WHITE", "user2"));
    }

    // ---------- getList ----------

    @Test
    @Order(7)
    @DisplayName("getList - Positive")
    public void getListPositive() throws DataAccessException {
        gameDAO.addGame(new GameData("g1"));
        gameDAO.addGame(new GameData("g2"));

        ArrayList<GameData> games = gameDAO.getList();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    @Order(8)
    @DisplayName("getList - Negative (empty list)")
    public void getListNegative() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.getList();
        Assertions.assertTrue(games.isEmpty());
    }

    // ---------- clearAllGames ----------

    @Test
    @Order(9)
    @DisplayName("clearAllGames - Positive")
    public void clearAllGamesPositive() throws DataAccessException {
        gameDAO.addGame(new GameData("clearGame"));
        gameDAO.clearAllGames();

        Assertions.assertTrue(gameDAO.getList().isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("clearAllGames - Negative (already empty)")
    public void clearAllGamesNegative() {
        Assertions.assertDoesNotThrow(() -> gameDAO.clearAllGames());
    }
}
