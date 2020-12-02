package com.highcrit.ffacheckers.socket.game.objects;

import java.util.ArrayList;
import java.util.List;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {
  private final List<Piece> pieces =
      new ArrayList<>() {
        {
          add(new Piece(PlayerColor.RED, 94, true));
          add(new Piece(PlayerColor.YELLOW, 2, false));
          add(new Piece(PlayerColor.BLUE, 6, false));
          add(new Piece(PlayerColor.YELLOW, 158, false));
          add(new Piece(PlayerColor.YELLOW, 155, true));
        }
      };

  @Test
  void toFen() {
    Board board = new Board(pieces);
    Assertions.assertNotNull(board);
    Board constructed = Board.fromFen(board.toFen());
    Assertions.assertNotNull(constructed);
    pieces.forEach(
        piece -> Assertions.assertEquals(constructed.getBoard()[piece.getPosition()], piece));
  }

  @Test
  void fromFen() {
    String FEN = "Y2,K155,158:B6:RK94";
    Board board = Board.fromFen(FEN);
    Assertions.assertNotNull(board);
    pieces.forEach(piece -> Assertions.assertEquals(board.getBoard()[piece.getPosition()], piece));
  }
}
