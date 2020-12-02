package com.highcrit.ffacheckers.socket.game.objects;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.GameState;
import com.highcrit.ffacheckers.socket.server.objects.AbstractClient;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;

public class Game {
  public static final int MAX_PLAYERS = 4;
  private static final String DEFAULT_FEN =
      "Y128,129,130,131,132,137,138,139,140,141,146,147,148,149,150,155,156,157,"
          + "158,159:B36,37,45,46,54,55,63,64,72,73,81,82,90,91,99,100,108,109,117,118:G2,3,4,5,6,11,12,13,14,15,"
          + "20,21,22,23,24,29,30,31,32,33:R43,44,52,53,61,62,70,71,79,80,88,89,97,98,106,107,115,116,124,125";

  private final HashMap<PlayerColor, AbstractClient> players = new HashMap<>();
  private Lobby lobby;

  private Board board;
  
  private boolean hasStarted = false;
  private PlayerColor currentPlayer = PlayerColor.YELLOW;
  private GameState gameState = GameState.WAITING;

  public Game(Lobby lobby) {
    this.lobby = lobby;
  }

  public void start() {
    start(DEFAULT_FEN);
  }

  public void start(String fen) {
    if (hasStarted) throw new IllegalStateException();
    hasStarted = true;
    board = Board.fromFen(fen);
  }


  public void onPlayerLoaded(AbstractClient info) {
    if (hasStarted()) {
      // TODO: Send game info
    } else {
      players.values().stream().filter(info::equals).findFirst().ifPresent(abstractPlayerClient -> abstractPlayerClient.setLoaded(true));
    }
  }

  public void addPlayer(AbstractClient playerClient) {
    for (PlayerColor playerColor : PlayerColor.values()) {
      if (players.get(playerColor) == null) {
        players.put(playerColor, playerClient);
        playerClient.setPlayerColor(playerColor);
      }
    }
  }

  public void removePlayer(AbstractClient info) {
    Optional<AbstractClient> oClient = players.values().stream().filter(info::equals).findFirst();
    if (oClient.isPresent()) {
      AbstractClient client = oClient.get();
      // TODO: Remove player pieces on his turn
    }
  }

  private void setGameState(GameState gameState) {
    this.gameState = gameState;
    lobby.send("game-state", gameState);
  }

  public boolean hasStarted() {
    return hasStarted;
  }

  public HashMap<PlayerColor, AbstractClient> getPlayers() {
    return players;
  }
}
