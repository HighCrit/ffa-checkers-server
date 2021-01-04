package com.highcrit.ffacheckers.domain.entities;

import java.util.stream.Stream;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PieceTest {

  private static Stream<Arguments> piecesSource() {
    return Stream.of(
        Arguments.of(
            "K89",
            new Piece(PlayerColor.YELLOW, 89, true),
            Arguments.of("8", new Piece(PlayerColor.YELLOW, 8))));
  }

  @ParameterizedTest
  @MethodSource("piecesSource")
  void testToString(String expected, Piece piece) {
    Assertions.assertEquals(expected, piece.toString());
  }
}
