package com.highcrit.ffacheckers.socket.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.highcrit.ffacheckers.domain.enums.Direction;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.domain.objects.Board;
import com.highcrit.ffacheckers.domain.objects.Piece;
import com.highcrit.ffacheckers.domain.objects.moves.CapturingMove;
import com.highcrit.ffacheckers.domain.objects.moves.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveCalculatorTest {

  private static final MoveCalculator moveCalculator = new MoveCalculator();

  @Test
  void getCapturingMovesTwoJumpsTwoEnds() {
    List<Piece> pieces =
        new ArrayList<>() {
          {
            add(new Piece(PlayerColor.YELLOW, 30));
            add(new Piece(PlayerColor.BLUE, 39));
            add(new Piece(PlayerColor.BLUE, 56));
            add(new Piece(PlayerColor.BLUE, 57));
          }
        };
    List<Move> expectedMoves =
        new ArrayList<>() {
          {
            add(new CapturingMove(30, Arrays.asList(Direction.SOUTHWEST, Direction.SOUTHEAST)));
            add(new CapturingMove(30, Arrays.asList(Direction.SOUTHWEST, Direction.SOUTHWEST)));
          }
        };
    Board board = new Board(pieces);
    List<CapturingMove> moves = moveCalculator.getCapturingMoves(board, PlayerColor.YELLOW, 30);
    Assertions.assertIterableEquals(expectedMoves, moves, () -> Arrays.toString(moves.toArray()));
  }
}
