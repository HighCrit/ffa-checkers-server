package com.highcrit.ffacheckers.socket.utils;

import java.util.stream.Stream;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RankCalculatorTest {

  private static Stream<Arguments> indexToRank() {
    return Stream.of(
        Arguments.of(1, PlayerColor.GREEN, 2),
        Arguments.of(1, PlayerColor.GREEN, 5),
        Arguments.of(2, PlayerColor.GREEN, 11),
        Arguments.of(2, PlayerColor.GREEN, 14),
        Arguments.of(12, PlayerColor.GREEN, 103),
        Arguments.of(13, PlayerColor.GREEN, 110),
        Arguments.of(1, PlayerColor.BLUE, 36),
        Arguments.of(1, PlayerColor.BLUE, 54),
        Arguments.of(2, PlayerColor.BLUE, 45),
        Arguments.of(2, PlayerColor.BLUE, 99),
        Arguments.of(14, PlayerColor.BLUE, 105),
        Arguments.of(15, PlayerColor.BLUE, 79),
        Arguments.of(1, PlayerColor.RED, 53),
        Arguments.of(1, PlayerColor.RED, 107),
        Arguments.of(2, PlayerColor.RED, 62),
        Arguments.of(2, PlayerColor.RED, 80),
        Arguments.of(14, PlayerColor.RED, 92),
        Arguments.of(15, PlayerColor.RED, 46));
  }

  @ParameterizedTest
  @MethodSource("indexToRank")
  void getRankFromIndexForColor(int expected, PlayerColor color, int index) {
    Assertions.assertEquals(expected, RankCalculator.getRankFromIndexForColor(color, index));
  }
}
