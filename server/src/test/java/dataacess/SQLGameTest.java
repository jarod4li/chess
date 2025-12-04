package dataacess;

import dataaccess.DataAccessException;
import dataaccess.GameSQL;
import model.GameData;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLGameTest {

    private static final String TEST_GAME_NAME = "testGame";
    private static final String TEST_GAME_ID = "1";

    private GameSQL gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new GameSQL();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        gameDAO.clearAllGames();
    }

    private void insertTestGame() throws DataAccessException {
        gameDAO.addGame(new GameData(TEST_GAME_NAME));
    }

    private void assertTestGameRetrieved() throws DataAccessException {
        GameData retrieved = gameDAO.getGame(TEST_GAME_ID);
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals(TEST_GAME_NAME, retrieved.getName());
    }

    @Test
    @Order(1)
    @DisplayName("addGame - Positive")
    public void addGamePositive() throws DataAccessException {
        insertTestGame();
        assertTestGameRetrieved();
    }

    @Test
    @Order(2)
    @DisplayName("addGame - Negative (null name)")
    public void addGameNegative() {
        Assertions.assertThrows(DataAccessException.class, () ->
                gameDAO.addGame(new GameData(null, null, null, null)));
    }

    @Test
    @Order(3)
    @DisplayName("getGame - Positive")
    public void getGamePositive() throws DataAccessException {
        insertTestGame();
        assertTestGameRetrieved();
    }

    @Test
    @Order(4)
    @DisplayName("getGame - Negative (not found)")
    public void getGameNegative() throws DataAccessException {
        GameData game = gameDAO.getGame("999");
        Assertions.assertNull(game);
    }

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
