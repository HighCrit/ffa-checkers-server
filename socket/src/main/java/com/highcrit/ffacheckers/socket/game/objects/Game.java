package com.highcrit.ffacheckers.socket.game.objects;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.domain.entities.Replay;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.GameEvent;
import com.highcrit.ffacheckers.socket.game.enums.GameState;
import com.highcrit.ffacheckers.socket.game.instances.MoveCalculator;
import com.highcrit.ffacheckers.socket.game.objects.data.MoveResult;
import com.highcrit.ffacheckers.socket.game.objects.moves.MoveSequence;
import com.highcrit.ffacheckers.socket.lobby.objects.ILobby;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.objects.clients.AbstractClient;
import com.highcrit.ffacheckers.socket.utils.WebManager;
import lombok.Getter;

public class Game {
  public static final int MAX_PLAYERS = 4;
  private static final MoveCalculator MOVE_CALCULATOR = new MoveCalculator();
  private static final String DEFAULT_FEN =
      "Y128,129,130,131,132,137,138,139,140,141,146,147,148,149,150,155,156,157,"
          + "158,159:B36,37,45,46,54,55,63,64,72,73,81,82,90,91,99,100,108,109,117,118:G2,3,4,5,6,11,12,13,14,15,"
          + "20,21,22,23,24,29,30,31,32,33:R43,44,52,53,61,62,70,71,79,80,88,89,97,98,106,107,115,116,124,125";

  @Getter
  private final EnumMap<PlayerColor, AbstractClient> players = new EnumMap<>(PlayerColor.class);

  private final ILobby lobby;

  // Save the move sets so we don't have to recalculate
  private List<Move> normalMoves = Collections.emptyList();
  private List<MoveSequence> capturingMoves = Collections.emptyList();

  private MoveSequence lastMoveSequence;
  private Board board;
  @Getter private boolean hasStarted = false;
  private PlayerColor currentPlayer = PlayerColor.RED;
  private GameState gameState = GameState.WAITING;

  public Game(Lobby lobby) {
    this.lobby = lobby;
  }

  public void start(String fen) {
    if (hasStarted) throw new IllegalStateException();
    hasStarted = true;
    board = Board.fromFen(fen);
    setGameState(GameState.PLAYING);
    lobby.send(GameEvent.BOARD, board.toFen());
    startNextTurn();
  }

  public void onPlayerLoaded(AbstractClient info) {
    players.values().stream()
        .filter(info::equals)
        .findFirst()
        .ifPresent(
            abstractPlayerClient -> {
              abstractPlayerClient.setLoaded(true);
              abstractPlayerClient.setGone(false);
            });
    info.send(GameEvent.YOUR_COLOR, info.getPlayerColor());
    if (gameState == GameState.PLAYING) {
      info.send(GameEvent.STATE, gameState);
      info.send(GameEvent.YOUR_COLOR, info.getPlayerColor());
      info.send(GameEvent.BOARD, board.toFen());
      info.send(GameEvent.CURRENT_PLAYER, currentPlayer);

      if (currentPlayer == info.getPlayerColor()) {
        info.send(GameEvent.MOVE_SET, capturingMoves.isEmpty() ? normalMoves : capturingMoves);
      }
    } else {
      if (players.values().stream().allMatch(AbstractClient::isLoaded)
          && players.size() == MAX_PLAYERS) {
        start(DEFAULT_FEN);
      }
    }
  }

  public void addPlayer(AbstractClient playerClient) {
    if (hasStarted) return;
    for (PlayerColor playerColor : PlayerColor.values()) {
      if (players.get(playerColor) == null) {
        players.put(playerColor, playerClient);
        playerClient.setPlayerColor(playerColor);
        return;
      }
    }
  }

  public void startNextTurn() {
    // Set currentPlayer to next color
    currentPlayer =
        PlayerColor.values()[(currentPlayer.ordinal() + 1) % PlayerColor.values().length];

    // If the current player doesn't exist or has left
    if (players.get(currentPlayer) == null || players.get(currentPlayer).isGone()) {
      // But they still have pieces on the board
      startNextTurn();
      return;
    }

    capturingMoves = MOVE_CALCULATOR.getCapturingMoves(board, currentPlayer);

    // If no jumps are possible
    if (capturingMoves.isEmpty()) {
      // Calculate normal moves
      normalMoves = MOVE_CALCULATOR.getNormalMoves(board, currentPlayer);
      if (normalMoves.isEmpty()) {
        // Player lost, no legal moves available
        players.get(currentPlayer).setGone(true);
        lobby.send(GameEvent.BOARD, board.toFen());
        hasGameEnded();
        startNextTurn();
      } else {
        lobby.send(GameEvent.CURRENT_PLAYER, currentPlayer);
        players.get(currentPlayer).send(GameEvent.MOVE_SET, normalMoves);
      }
    } else {
      normalMoves.clear();
      lastMoveSequence = new MoveSequence();
      lobby.send(GameEvent.CURRENT_PLAYER, currentPlayer);
      players
          .get(currentPlayer)
          .send(
              GameEvent.MOVE_SET,
              capturingMoves.stream()
                  .map(ms -> ms.getSequence().get(0))
                  .collect(Collectors.toList()));
    }
  }

  public void onMove(AbstractClient client, Move move) {
    if (currentPlayer != client.getPlayerColor()) {
      client.send(GameEvent.MOVE_RESULT, new ActionFailed("It's not your turn"));
      return;
    }

    // Check whether we expect a capturing move or not
    if (normalMoves.isEmpty()) {
      // Construct sequence using past moves
      lastMoveSequence.addMove(move);
      String tempMoveSequenceString = lastMoveSequence.toString();
      // Find sequence in expected sequences using constructed sequence
      MoveSequence ms =
          capturingMoves.stream()
              .filter(s -> s.toString().startsWith(tempMoveSequenceString))
              .findFirst()
              .orElse(null);
      // If sequence is valid
      if (ms == null) {
        // No sequence was found
        lastMoveSequence.undoMove();
        return;
      }
      // Get current move to execute
      Move cMove = ms.getSequence().get(lastMoveSequence.length() - 1);
      board.applyMove(cMove);
      lobby.send(GameEvent.MOVE_RESULT, new MoveResult(cMove));
      // Check is sequence has been completed
      if (ms.toString().equals(tempMoveSequenceString)) {
        // Only promote at end of sequence
        if (cMove.isPromoting()) {
          board.getGrid()[cMove.getEnd()].setKing(true);
        }
        startNextTurn();
      } else {
        // Send next move in sequence
        client.send(
            GameEvent.MOVE_SET,
            capturingMoves.stream()
                .map(m -> m.getSequence().get(lastMoveSequence.length()))
                .collect(Collectors.toList()));
      }
    } else {
      // Normal moves
      // Find fully constructed move from given move
      Move nMove =
          normalMoves.stream()
              .filter(m -> m.toString().equals(move.toString()))
              .findFirst()
              .orElse(null);
      // Check if move was found
      if (nMove == null) {
        // No valid moves were found for given move information
        client.send(GameEvent.MOVE_RESULT, new ActionFailed("Invalid move"));
        return;
      }
      board.applyMove(nMove);
      lobby.send(GameEvent.MOVE_RESULT, new MoveResult(nMove));
      if (nMove.isPromoting()) {
        board.getGrid()[nMove.getEnd()].setKing(true);
      }
      startNextTurn();
    }
  }

  public void removePlayer(AbstractClient info) {
    Optional<AbstractClient> oClient = players.values().stream().filter(info::equals).findFirst();
    if (oClient.isPresent()) {
      AbstractClient client = oClient.get();
      client.setGone(true);
      players.remove(info.getPlayerColor());

      if (info.getPlayerColor() == currentPlayer) {
        startNextTurn();
      }
    }
  }

  public void hasGameEnded() {
    if (!hasStarted) return;
    List<AbstractClient> playingPlayers =
        this.players.values().stream().filter(p -> !p.isGone()).collect(Collectors.toList());
    if (playingPlayers.size() == 1) {
      setGameState(GameState.ENDED);
      lobby.send(GameEvent.WON, playingPlayers.get(0).getPlayerColor());
      WebManager.saveReplay(
          new Replay(lobby.getCode(), board.getInitialFen(), board.getMoveHistory()));
      lobby.onGameEnd();
    }
  }

  private void setGameState(GameState gameState) {
    this.gameState = gameState;
    lobby.send(GameEvent.STATE, gameState);
  }
}
