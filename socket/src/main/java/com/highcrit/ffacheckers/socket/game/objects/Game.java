package com.highcrit.ffacheckers.socket.game.objects;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.highcrit.ffacheckers.domain.entities.Replay;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.domain.entities.Piece;
import com.highcrit.ffacheckers.socket.game.enums.GameEvent;
import com.highcrit.ffacheckers.socket.game.enums.GameState;
import com.highcrit.ffacheckers.socket.game.instances.MoveCalculator;
import com.highcrit.ffacheckers.socket.game.objects.data.MoveResult;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.socket.game.objects.moves.MoveSequence;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.objects.AbstractClient;
import com.highcrit.ffacheckers.socket.utils.RankCalculator;
import com.highcrit.ffacheckers.socket.utils.WebManager;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;

public class Game {
  public static final int MAX_PLAYERS = 4;
  private static final MoveCalculator MOVE_CALCULATOR = new MoveCalculator();
  private static final String DEFAULT_FEN =
      "Y128,129,130,131,132,137,138,139,140,141,146,147,148,149,150,155,156,157,"
          + "158,159:B36,37,45,46,54,55,63,64,72,73,81,82,90,91,99,100,108,109,117,118:G2,3,4,5,6,11,12,13,14,15,"
          + "20,21,22,23,24,29,30,31,32,33:R43,44,52,53,61,62,70,71,79,80,88,89,97,98,106,107,115,116,124,125";

  private final EnumMap<PlayerColor, AbstractClient> players = new EnumMap<>(PlayerColor.class);
  private final Lobby lobby;

  // Save the move sets so we don't have to recalculate
  private List<Move> normalMoves = Collections.emptyList();
  private List<MoveSequence> capturingMoves = Collections.emptyList();
  private List<String> capturingMoveStrings = Collections.emptyList();

  private MoveSequence lastMoveSequence;
  private Board board;
  private boolean hasStarted = false;
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
            .ifPresent(abstractPlayerClient -> {
              abstractPlayerClient.setLoaded(true);
              abstractPlayerClient.setLeft(false);
            });
    info.send(GameEvent.YOUR_COLOR, info.getPlayerColor());
    if (hasStarted) {
      info.send(GameEvent.STATE, gameState);
      info.send(GameEvent.YOUR_COLOR, info.getPlayerColor());
      info.send(GameEvent.BOARD, board.toFen());
      info.send(GameEvent.CURRENT_PLAYER, currentPlayer);

      if (currentPlayer == info.getPlayerColor()) {
        info.send(GameEvent.MOVE_SET, capturingMoves.isEmpty() ? normalMoves : capturingMoves);
      }
    } else {
      if (players.values().stream().allMatch(AbstractClient::isLoaded) && players.size() == MAX_PLAYERS) {
        start(DEFAULT_FEN);
      }
    }
  }

  public void addPlayer(AbstractClient playerClient) {
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
    currentPlayer = PlayerColor.values()[(currentPlayer.ordinal() + 1) % PlayerColor.values().length];

    // If the current player doesn't exist or has left
    if (players.get(currentPlayer) == null || players.get(currentPlayer).hasLeft()) {
      // But they still have pieces on the board
      if (board.getPieces().get(currentPlayer) != null) {
        // Remove them and re-emit the board
        board.removePlayer(currentPlayer);
        lobby.send(GameEvent.BOARD, board.toFen());
        hasGameEnded();
      }
      startNextTurn();
      return;
    }

    capturingMoves = MOVE_CALCULATOR.getCapturingMoves(board, currentPlayer);
    capturingMoveStrings = capturingMoves.stream().map(MoveSequence::toString).collect(Collectors.toList());

    // If no jumps are possible
    if (capturingMoves.isEmpty()) {
      // Calculate normal moves
      normalMoves = MOVE_CALCULATOR.getNormalMoves(board, currentPlayer);
      if (normalMoves.isEmpty()) {
        // Player lost, no legal moves available
        players.get(currentPlayer).setLeft(true);
        board.removePlayer(currentPlayer);
        lobby.send(GameEvent.BOARD, board.toFen());
        startNextTurn();
        hasGameEnded();
      } else {
        lobby.send(GameEvent.CURRENT_PLAYER, currentPlayer);
        players.get(currentPlayer).send(GameEvent.MOVE_SET, normalMoves);
      }
    } else {
      normalMoves.clear();
      lastMoveSequence = new MoveSequence();
      lobby.send(GameEvent.CURRENT_PLAYER, currentPlayer);
      players.get(currentPlayer).send(GameEvent.MOVE_SET, capturingMoves);
    }
  }

  public void onMove(AbstractClient client, Move move) {
    if (currentPlayer != client.getPlayerColor() || move.getStart() < 0 || move.getStart() > 161 || move.getEnd() < 0 || move.getEnd() > 161) {
      client.send(GameEvent.MOVE_RESULT, new ActionFailed("Invalid move"));
      return;
    }

    // Check if move is legal
    if (normalMoves.isEmpty()) {
      // Sequences
      lastMoveSequence.addMove(move);
      String tempMoveSequenceString = lastMoveSequence.toString();
      if (capturingMoveStrings.stream().anyMatch(ms -> ms.startsWith(tempMoveSequenceString))) {
        // Execute, it's either a or a start of a valid sequence
        board.applyMove(move);
        lobby.send(GameEvent.MOVE_RESULT, new MoveResult(move));
        if (capturingMoveStrings.contains(tempMoveSequenceString)) {
          checkPiecePromotion(move.getEnd());
          // Sequence complete
          startNextTurn();
        }
      }
    } else {
      // Normal moves
      if (normalMoves.contains(move)) {
        board.applyMove(move);
        lobby.send(GameEvent.MOVE_RESULT, new MoveResult(move));
        checkPiecePromotion(move.getEnd());
        startNextTurn();
      } else {
        client.send(GameEvent.MOVE_RESULT, new ActionFailed("Invalid move"));
      }
    }
  }

  private void checkPiecePromotion(int index) {
    Piece piece = board.getGrid()[index];
    if (RankCalculator.getRankFromIndexForColor(piece.getPlayerColor(), index) >= 10
        && !piece.isKing()) {
      piece.makeKing();
      lobby.send(GameEvent.PIECE_PROMOTION, index);
    }
  }

  public void removePlayer(AbstractClient info) {
    Optional<AbstractClient> oClient = players.values().stream().filter(info::equals).findFirst();
    if (oClient.isPresent()) {
      AbstractClient client = oClient.get();
      client.setLeft(true);
      players.remove(info.getPlayerColor());

      if (info.getPlayerColor() == currentPlayer) {
        startNextTurn();
      }
    }
  }

  public void hasGameEnded() {
    if (!hasStarted) return;
    List<AbstractClient> playingPlayers =
            this.players.values().stream().filter(p -> !p.hasLeft()).collect(Collectors.toList());
    if (playingPlayers.size() == 1) {
      setGameState(GameState.ENDED);
      lobby.send(GameEvent.WON, playingPlayers.get(0).getPlayerColor());
      WebManager.saveReplay(new Replay(lobby.getCode(), board.getMoveHistory()));
    }
  }

  private void setGameState(GameState gameState) {
    this.gameState = gameState;
    lobby.send(GameEvent.STATE, gameState);
  }

  public boolean hasStarted() {
    return hasStarted;
  }

  public Map<PlayerColor, AbstractClient> getPlayers() {
    return players;
  }
}
