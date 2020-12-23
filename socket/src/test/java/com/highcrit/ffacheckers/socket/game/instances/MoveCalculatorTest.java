package com.highcrit.ffacheckers.socket.game.instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.objects.Board;
import com.highcrit.ffacheckers.domain.entities.Piece;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.socket.game.objects.moves.MoveSequence;
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
    List<MoveSequence> expectedMoves =
        new ArrayList<>() {
          {
            add(
                new MoveSequence(
                    new LinkedList<>(
                        Arrays.asList(
                            new Move(30, 47, pieces.get(1)), new Move(47, 66, pieces.get(3))))));
            add(
                new MoveSequence(
                    new LinkedList<>(
                        Arrays.asList(
                            new Move(30, 47, pieces.get(1)), new Move(47, 64, pieces.get(2))))));
          }
        };
    Board board = new Board(pieces);
    Assertions.assertIterableEquals(
        expectedMoves, moveCalculator.getCapturingMoves(board, PlayerColor.YELLOW));
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
            add(new Move(121, 113));
            add(new Move(121, 112));
            add(new Move(31, 23));
            add(new Move(31, 41));
            add(new Move(31, 40));
            add(new Move(31, 22));
          }
        };
    Board board = new Board(pieces);
    Assertions.assertIterableEquals(
        expectedMoves, moveCalculator.getNormalMoves(board, PlayerColor.YELLOW));
  }
}
