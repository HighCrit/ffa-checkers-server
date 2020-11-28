package com.highcrit.ffacheckers.socket.handlers;

import java.util.ArrayList;
import java.util.List;

import com.highcrit.ffacheckers.domain.enums.Direction;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.domain.objects.Board;
import com.highcrit.ffacheckers.domain.objects.Piece;
import com.highcrit.ffacheckers.domain.objects.moves.CapturingMove;
import com.highcrit.ffacheckers.domain.objects.moves.Move;

public class MoveCalculator {
  public List<Move> getMoves(PlayerColor playerColor, Board board) {
    List<Move> moves = new ArrayList<>();

    return moves;
  }

  public List<CapturingMove> getCapturingMoves(Board board, PlayerColor playerColor, int origin) {
    ArrayList<CapturingMove> moves = new ArrayList<>();
    getCapturingMoves(board, playerColor, moves, origin, origin, new CapturingMove(origin));
    return moves;
  }

  private void getCapturingMoves(
      Board board,
      PlayerColor playerColor,
      List<CapturingMove> moves,
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
      getCapturingMoves(board, playerColor, moves, origin, endPos, prevMove);
      // Reset
      pieceAtPosInDir.setGhost(false);
      prevMove.undoJump();
    }

    if (!jumpFound) moves.add(prevMove.copy());
  }
}
