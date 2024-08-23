package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class GameDataDAO implements GameDAO{
  private ArrayList<GameData> gamesList =  new ArrayList<>();
  private int gameCount = 1;

  public void addGame(GameData newGame) {
    String gameID = String.valueOf(gameCount);
    newGame.setGameID(gameID);
    newGame.setGame();
    gameCount += 1;

    gamesList.add(newGame);

  }

  public String getGameID(String gameName) {
    for (GameData game: gamesList) {
      if (game.getName().equals(gameName)){
        return game.getGameID();
      }
    }
    return null;
  }

  public Boolean getGameName(String gameName){
    for (GameData game:gamesList) {
      if (game.getName().equals(gameName)){
        return true;
      }
    }
    return false;
  }

  public GameData getGame(String gameID) {
    for (GameData game:gamesList){
      if (game.getGameID().equals(gameID)){
        return game;
      }
    }
    return null;
  }

  public Boolean setGame(GameData currGame, String playerColor, String username) {
    GameData game = getGame(currGame.getGameID());
    if (game == null){
      return false;
    }

    if (playerColor==null){
      return true;
    }
    else if (playerColor.equals("WHITE") && (game.getWhite() == null)) {
      game.setWhite(username);
      return true;
    } else if (playerColor.equals("BLACK") && (game.getBlack() == null)) {
      game.setBlack(username);
      return true;
    }


    return false;
  }

  public ArrayList<GameData> getList(){
    return gamesList;
  }

  public void clearAllGames(){
    gamesList.clear();
  }
  public void updateGame(String gameID, GameData updatedGame){
    for (GameData game:gamesList){
      if (game.getGameID().equals(gameID)){
        gamesList.remove(game);
        gamesList.add(updatedGame);
      }
    }
  }
}
