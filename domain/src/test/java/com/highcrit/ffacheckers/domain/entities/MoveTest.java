package com.highcrit.ffacheckers.domain.entities;

import java.util.stream.Stream;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MoveTest {

  private static Stream<Arguments> moves() {
    return Stream.of(
        Arguments.of(new Move(2, 11), "2-11"),
        Arguments.of(new Move(2, 21, new Piece(PlayerColor.RED, 11)), "2x21"));
  }

  @ParameterizedTest
  @MethodSource("moves")
  void testToString(Move move, String expected) {
    Assertions.assertEquals(expected, move.toString());
  }
}
