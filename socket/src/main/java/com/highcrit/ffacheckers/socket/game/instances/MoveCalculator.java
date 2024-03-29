package com.highcrit.ffacheckers.socket.game.instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.domain.entities.Piece;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.Direction;
import com.highcrit.ffacheckers.socket.game.objects.Board;
import com.highcrit.ffacheckers.socket.game.objects.moves.MoveSequence;
import com.highcrit.ffacheckers.socket.utils.RankCalculator;

public class MoveCalculator {
  /**
   * List of all directions a normal piece may move in
   */
  private static final EnumMap<PlayerColor, List<Direction>> playerDirectionMap =
      new EnumMap<>(PlayerColor.class);

  static {
    playerDirectionMap.put(
        PlayerColor.YELLOW, Arrays.asList(Direction.UP_LEFT, Direction.UP_RIGHT));
    playerDirectionMap.put(
        PlayerColor.BLUE, Arrays.asList(Direction.UP_RIGHT, Direction.DOWN_RIGHT));
    playerDirectionMap.put(
        PlayerColor.GREEN, Arrays.asList(Direction.DOWN_LEFT, Direction.DOWN_RIGHT));
    playerDirectionMap.put(PlayerColor.RED, Arrays.asList(Direction.UP_LEFT, Direction.DOWN_LEFT));
  }

  /**
   * Returns an array of the longest capturing sequences available for a given player on a board
   * @param board board instance
   * @param playerColor player
   * @return array of longest capturing sequences
   */
  public List<MoveSequence> getCapturingMoves(Board board, PlayerColor playerColor) {
    List<MoveSequence> moveSequences = new ArrayList<>();
    List<Piece> deSyncedPieces = new ArrayList<>();

    for (Piece piece : board.getPieces().get(playerColor)) {
      Piece p = board.getGrid()[piece.getPosition()];
      if (p == null) {
        deSyncedPieces.add(piece);
      } else {
        getMoveSequenceOfPiece(board, p, new LinkedList<>(), moveSequences);
      }
    }

    board.getPieces().get(playerColor).removeAll(deSyncedPieces);

    // Only longest sequence may be played
    int maxLength =
        moveSequences.stream().mapToInt(MoveSequence::length).max().orElse(Integer.MAX_VALUE);
    return moveSequences.stream()
        .filter(ms -> ms.length() == maxLength)
        .collect(Collectors.toList());
  }

  /**
   * Creates a list of capturing sequences for a given piece on a board
   * @param board board instance
   * @param piece piece instance
   * @param pastMoves moves made by previous iterations
   * @param moveSequences list of capturing sequences
   */
  private void getMoveSequenceOfPiece(
      Board board, Piece piece, LinkedList<Move> pastMoves, List<MoveSequence> moveSequences) {
    boolean jumpFound = false;

    for (Direction direction : Direction.values()) {
      int positionInDirection = direction.getIndexInDirectionFrom(piece.getPosition());

      if (positionInDirection != -1) { // If position is valid
        Piece pieceAtPositionInDirection = board.getGrid()[positionInDirection];

        if (pieceAtPositionInDirection == null // If there is hittable piece
            || pieceAtPositionInDirection.isGhost()
            || pieceAtPositionInDirection.getPlayerColor() == piece.getPlayerColor()) {
          continue;
        }
        // Check if place after piece is available to jump to
        int positionAfterJump = direction.getIndexInDirectionFrom(positionInDirection);

        if (positionAfterJump != -1
            && board.getGrid()[positionAfterJump] == null) { // if can jump to valid spot
          // Move from start to position after jump is a valid move
          jumpFound = true;
          Move move =
              new Move(piece.getPosition(), positionAfterJump, pieceAtPositionInDirection.copyOf());
          pastMoves.add(move);
          board.applyMove(move);
          getMoveSequenceOfPiece(board, piece, pastMoves, moveSequences);
          board.undoMove();
          pastMoves.removeLast();
        }
      }
    }
    if (!jumpFound && !pastMoves.isEmpty()) {
      if (!piece.isKing()
          && RankCalculator.getRankFromIndexForColor(
                  piece.getPlayerColor(), pastMoves.getLast().getEnd())
              >= 10) {
        pastMoves.getLast().setPromoting(true);
      }
      moveSequences.add(new MoveSequence(pastMoves));
    }
  }

  /**
   * Returns a list of all normal moves available to a player on a given board
   * @param board board instance
   * @param playerColor player color
   * @return all normal moves for player
   */
  public List<Move> getNormalMoves(Board board, PlayerColor playerColor) {
    List<Move> moves = new ArrayList<>();
    List<Piece> pieces = board.getPieces().get(playerColor);

    pieces.forEach(
        piece -> {
          for (Direction dir : Direction.values()) {
            if (piece.isKing() || playerDirectionMap.get(playerColor).contains(dir)) {
              int posInDir = dir.getIndexInDirectionFrom(piece.getPosition());
              if (posInDir != -1 && board.getGrid()[posInDir] == null) {
                moves.add(
                    new Move(
                        piece.getPosition(),
                        posInDir,
                        RankCalculator.getRankFromIndexForColor(playerColor, posInDir) >= 10));
              }
            }
          }
        });

    return moves;
  }
}
