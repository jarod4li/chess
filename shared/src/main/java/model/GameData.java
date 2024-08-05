package model;

import chess.ChessGame;

import java.util.Objects;

public class GameData {
  private String whiteUsername;
  private String blackUsername;
  private String gameName;
  private ChessGame game;

    this.gameID = gameID;
  }

  }
    this.whiteUsername = whiteUsername;
    this.blackUsername = blackUsername;
    this.gameName = gameName;
  }

  public ChessGame getGame(){
    return game;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameData gameData = (GameData) o;
  }

  @Override
  public int hashCode() {
  }

  @Override
  public String toString() {
    return "GameData{" +
            ", whiteUsername='" + whiteUsername + '\'' +
            ", blackUsername='" + blackUsername + '\'' +
            '}';
  }
}
