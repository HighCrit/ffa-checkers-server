package com.highcrit.ffacheckers.socket.game.instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.Direction;
import com.highcrit.ffacheckers.socket.game.objects.Board;
import com.highcrit.ffacheckers.socket.game.objects.Piece;
import com.highcrit.ffacheckers.socket.game.objects.moves.CapturingMove;
import com.highcrit.ffacheckers.socket.game.objects.moves.Move;
import com.highcrit.ffacheckers.socket.game.objects.moves.NormalMove;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveCalculatorTest {

  private static final MoveCalculator moveCalculator = new MoveCalculator();

  @Test
  void getCapturingMoves() {
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
            add(new CapturingMove(30, Arrays.asList(Direction.DOWN_LEFT, Direction.DOWN_RIGHT)));
            add(new CapturingMove(30, Arrays.asList(Direction.DOWN_LEFT, Direction.DOWN_LEFT)));
          }
        };
    Board board = new Board(pieces);
    List<? extends Move> capturingMoves = moveCalculator.getMoves(board, PlayerColor.YELLOW);
    Assertions.assertIterableEquals(expectedMoves, capturingMoves);
  }

  @Test
  void getNormalMoves() {
    List<Piece> pieces =
        new ArrayList<>() {
          {
            add(new Piece(PlayerColor.YELLOW, 36));
            add(new Piece(PlayerColor.YELLOW, 121));
            add(new Piece(PlayerColor.YELLOW, 31, true));
          }
        };
    List<Move> expectedMoves =
        new ArrayList<>() {
          {
            add(new NormalMove(121, Direction.UP_RIGHT));
            add(new NormalMove(121, Direction.UP_LEFT));
            add(new NormalMove(31, Direction.UP_RIGHT));
            add(new NormalMove(31, Direction.DOWN_RIGHT));
            add(new NormalMove(31, Direction.DOWN_LEFT));
            add(new NormalMove(31, Direction.UP_LEFT));
          }
        };
    Board board = new Board(pieces);
    List<? extends Move> normalMoves = moveCalculator.getMoves(board, PlayerColor.YELLOW);
    Assertions.assertIterableEquals(expectedMoves, normalMoves, normalMoves::toString);
  }
}
