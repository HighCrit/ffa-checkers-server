package com.highcrit.ffacheckers.socket.game.instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.Direction;
import com.highcrit.ffacheckers.socket.game.objects.Board;
import com.highcrit.ffacheckers.socket.game.objects.Piece;
import com.highcrit.ffacheckers.socket.game.objects.moves.CapturingMove;
import com.highcrit.ffacheckers.socket.game.objects.moves.Move;
import com.highcrit.ffacheckers.socket.game.objects.moves.NormalMove;

public class MoveCalculator {
  private static final Map<PlayerColor, List<Direction>> playerDirectionMap =
      new HashMap<>() {
        {
          put(PlayerColor.YELLOW, Arrays.asList(Direction.UP_LEFT, Direction.UP_RIGHT));
          put(PlayerColor.BLUE, Arrays.asList(Direction.UP_RIGHT, Direction.DOWN_RIGHT));
          put(PlayerColor.GREEN, Arrays.asList(Direction.DOWN_LEFT, Direction.DOWN_RIGHT));
          put(PlayerColor.RED, Arrays.asList(Direction.UP_LEFT, Direction.DOWN_LEFT));
        }
      };

  public List<? extends Move> getMoves(Board board, PlayerColor playerColor) {
    List<CapturingMove> capturingMoves = getCapturingMoves(board, playerColor);

    if (capturingMoves.size() > 0) {
      int maxJumpCount =
          capturingMoves.stream()
              .max(Comparator.comparingInt(CapturingMove::getJumpCount))
              .get()
              .getJumpCount();

      capturingMoves.removeIf(m -> m.getJumpCount() != maxJumpCount);

      return capturingMoves;
    }

    return getNormalMoves(board, playerColor);
  }

  // TODO: De-smell
  public List<CapturingMove> getCapturingMoves(Board board, PlayerColor playerColor) {
    List<CapturingMove> capturingMoves = new ArrayList<>();

    board
        .getPieces()
        .get(playerColor)
        .forEach(
            piece ->
                getCapturingMovesOfPiece(
                    board,
                    playerColor,
                    capturingMoves,
                    piece.getPosition(),
                    piece.getPosition(),
                    new CapturingMove(piece.getPosition())));

    return capturingMoves;
  }

  private void getCapturingMovesOfPiece(
      Board board,
      PlayerColor playerColor,
      List<CapturingMove> capturingMoves,
      int origin,
      int startPos,
      CapturingMove prevMove) {

    boolean jumpFound = false;

    for (Direction direction : Direction.values()) {
      int posInDir = direction.getIndexInDirectionFrom(startPos);
      if (posInDir == -1) continue; // posInDir is outside of field

      Piece pieceAtPosInDir = board.getBoard()[posInDir];
      // Check if there is a piece to jump
      if (pieceAtPosInDir == null
          || pieceAtPosInDir.isGhost()
          || pieceAtPosInDir.getPlayerColor() == playerColor) continue;

      int endPos = direction.getIndexInDirectionFrom(posInDir);
      // Check if piece can be jumped
      if (endPos == -1 || (endPos != origin && board.getBoard()[endPos] != null)) continue;

      jumpFound = true;

      // Setup for Recursion
      pieceAtPosInDir.setGhost(true);
      prevMove.addJump(direction);
      // Recursion
      getCapturingMovesOfPiece(board, playerColor, capturingMoves, origin, endPos, prevMove);
      // Reset
      pieceAtPosInDir.setGhost(false);
      prevMove.undoJump();
    }

    if (!jumpFound && prevMove.getJumpCount() > 0) capturingMoves.add(prevMove.copy());
  }

  public List<NormalMove> getNormalMoves(Board board, PlayerColor playerColor) {
    List<NormalMove> moves = new ArrayList<>();
    List<Piece> pieces = board.getPieces().get(playerColor);

    pieces.forEach(
        piece -> {
          for (Direction dir : Direction.values()) {
            if (piece.isKing() || playerDirectionMap.get(playerColor).contains(dir)) {
              int posInDir = dir.getIndexInDirectionFrom(piece.getPosition());
              if (posInDir != -1 && board.getBoard()[posInDir] == null) {
                moves.add(new NormalMove(piece.getPosition(), dir));
              }
            }
          }
        });

    return moves;
  }
}
