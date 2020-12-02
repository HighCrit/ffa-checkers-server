package com.highcrit.ffacheckers.socket.game.objects.moves;

import com.highcrit.ffacheckers.socket.game.objects.moves.CapturingMove;
import com.highcrit.ffacheckers.socket.game.enums.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CapturingMoveTest {

  @Test
  void addJump() {
    CapturingMove move = new CapturingMove(48);
    move.addJump(Direction.UP_RIGHT);
    Assertions.assertEquals(31, move.getEnd());
    move.addJump(Direction.DOWN_RIGHT);
    Assertions.assertEquals(50, move.getEnd());
    move.addJump(Direction.UP_LEFT);
    Assertions.assertEquals(31, move.getEnd());
    move.addJump(Direction.DOWN_LEFT);
    Assertions.assertEquals(48, move.getEnd());
  }

  @Test
  void undoJump() {
    CapturingMove move = new CapturingMove(48);
    move.addJump(Direction.UP_RIGHT);
    move.addJump(Direction.DOWN_RIGHT);
    move.addJump(Direction.UP_LEFT);

    Assertions.assertEquals(31, move.getEnd());
    move.undoJump();
    Assertions.assertEquals(50, move.getEnd());
    move.undoJump();
    Assertions.assertEquals(31, move.getEnd());
  }

  @Test
  void testToString() {
    CapturingMove move = new CapturingMove(48);
    move.addJump(Direction.UP_RIGHT);
    move.addJump(Direction.DOWN_RIGHT);

    Assertions.assertEquals("48x31x50", move.toString());
  }
}
