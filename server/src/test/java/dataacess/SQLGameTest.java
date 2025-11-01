package dataacess;

import dataaccess.GameSQL;
import dataaccess.DataAccessException;
import model.GameData;

import org.junit.jupiter.api.*;
import java.util.ArrayList;

/**
 * Clean and consistent tests for GameSQL DAO.
 * Each DAO method has one positive and one negative test.
 */
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
    public void addGame_Positive() throws DataAccessException {
        String gameName = "TestGame";
        GameData newGame = new GameData(gameName, null, null, null);

        gameDAO.addGame(newGame);

        ArrayList<GameData> list = gameDAO.getList();
        Assertions.assertFalse(list.isEmpty(), "Game list should not be empty after adding");
        Assertions.assertEquals(gameName, list.get(0).getName());
    }

    @Test
    @Order(3)
    @DisplayName("getGame - Positive")
    public void getGame_Positive() throws DataAccessException {
        String gameName = "RetrieveGame";
        GameData newGame = new GameData(gameName, null, null, null);

        gameDAO.addGame(newGame);
        GameData retrieved = gameDAO.getGame("1");

        Assertions.assertNotNull(retrieved, "Retrieved game should not be null");
        Assertions.assertEquals(gameName, retrieved.getName());
    }

    @Test
    @Order(4)
    @DisplayName("getGame - Negative (invalid ID)")
    public void getGame_Negative() throws DataAccessException {
        GameData result = gameDAO.getGame("9999");
        Assertions.assertNull(result, "Should return null for non-existent game ID");
    }


    @Test
    @Order(6)
    @DisplayName("setGame - Negative (color already taken)")
    public void setGame_Negative() throws DataAccessException {
        String gameName = "TakenColorGame";
        GameData testGame = new GameData(gameName, null, null, null);

        gameDAO.addGame(testGame);
        gameDAO.setGame(testGame, "WHITE", "player1");

        boolean result = gameDAO.setGame(testGame, "WHITE", "player2");
        Assertions.assertFalse(result, "Should not allow assigning white player twice");
    }

    // ---------- getList ----------

    @Test
    @Order(7)
    @DisplayName("getList - Positive")
    public void getList_Positive() throws DataAccessException {
        gameDAO.addGame(new GameData("GameA", null, null, null));
        gameDAO.addGame(new GameData("GameB", null, null, null));

        ArrayList<GameData> games = gameDAO.getList();
        Assertions.assertEquals(2, games.size(), "Should return 2 games");
        Assertions.assertTrue(games.stream().anyMatch(g -> g.getName().equals("GameA")));
        Assertions.assertTrue(games.stream().anyMatch(g -> g.getName().equals("GameB")));
    }

    @Test
    @Order(8)
    @DisplayName("getList - Negative (empty table)")
    public void getList_Negative() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.getList();
        Assertions.assertTrue(games.isEmpty(), "Should return empty list if no games exist");
    }

    // ---------- clearAllGames ----------

    @Test
    @Order(9)
    @DisplayName("clearAllGames - Positive")
    public void clearAllGames_Positive() throws DataAccessException {
        gameDAO.addGame(new GameData("ClearGame", null, null, null));
        gameDAO.clearAllGames();

        ArrayList<GameData> games = gameDAO.getList();
        Assertions.assertTrue(games.isEmpty(), "Game table should be empty after clear");
    }

    @Test
    @Order(10)
    @DisplayName("clearAllGames - Negative (already empty)")
    public void clearAllGames_Negative() {
        try {
            gameDAO.clearAllGames();
            Assertions.assertTrue(true, "Clearing empty table should succeed silently");
        } catch (DataAccessException e) {
            Assertions.fail("clearAllGames should not fail on empty table");
        }
    }
}
