package com.highcrit.ffacheckers.socket.game.objects.moves;

import java.util.Collections;

import com.highcrit.ffacheckers.domain.entities.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveSequenceTest {

  @Test
  void testToString() {
    Assertions.assertEquals(
        "21x35", (new MoveSequence(Collections.singletonList(new Move(21, 35, false)))).toString());
  }
}
