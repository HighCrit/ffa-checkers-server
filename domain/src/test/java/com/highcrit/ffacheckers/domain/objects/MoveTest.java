package com.highcrit.ffacheckers.domain.objects;

import com.highcrit.ffacheckers.domain.enums.Direction;
import com.highcrit.ffacheckers.domain.objects.moves.CapturingMove;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveTest {

  @Test
  void addJump() {
    CapturingMove move = new CapturingMove(48);
    move.addJump(Direction.NORTHEAST);
    Assertions.assertEquals(31, move.getEnd());
    move.addJump(Direction.SOUTHEAST);
    Assertions.assertEquals(50, move.getEnd());
    move.addJump(Direction.NORTHWEST);
    Assertions.assertEquals(31, move.getEnd());
    move.addJump(Direction.SOUTHWEST);
    Assertions.assertEquals(48, move.getEnd());
  }

  @Test
  void undoJump() {
    CapturingMove move = new CapturingMove(48);
    move.addJump(Direction.NORTHEAST);
    move.addJump(Direction.SOUTHEAST);
    move.addJump(Direction.NORTHWEST);

    Assertions.assertEquals(31, move.getEnd());
    move.undoJump();
    Assertions.assertEquals(50, move.getEnd());
    move.undoJump();
    Assertions.assertEquals(31, move.getEnd());
  }

  @Test
  void testToString() {
    CapturingMove move = new CapturingMove(48);
    move.addJump(Direction.NORTHEAST);
    move.addJump(Direction.SOUTHEAST);

    Assertions.assertEquals("48x31x50", move.toString());
  }
}
