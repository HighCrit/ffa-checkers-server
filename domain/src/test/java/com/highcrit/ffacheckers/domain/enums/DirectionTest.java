package com.highcrit.ffacheckers.domain.enums;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DirectionTest {

  private static Stream<Arguments> getIndexSource() {
    return Stream.of(
        Arguments.of(new int[] {-1, 11, -1, -1}, 2),
        Arguments.of(new int[] {-1, 12, 11, -1}, 3),
        Arguments.of(new int[] {-1, 13, 12, -1}, 4),
        Arguments.of(new int[] {-1, 14, 13, -1}, 5),
        Arguments.of(new int[] {-1, 15, 14, -1}, 6),
        Arguments.of(new int[] {3, 21, 20, 2}, 11),
        Arguments.of(new int[] {-1, -1, 24, 6}, 15),
        Arguments.of(new int[] {-1, 43, 42, 24}, 33),
        Arguments.of(new int[] {29, 47, 46, -1}, 38),
        Arguments.of(new int[] {-1, 46, 45, -1}, 37),
        Arguments.of(new int[] {-1, 45, -1, -1}, 36),
        Arguments.of(new int[] {45, 63, -1, -1}, 54),
        Arguments.of(new int[] {63, 81, -1, -1}, 72),
        Arguments.of(new int[] {81, 99, -1, -1}, 90),
        Arguments.of(new int[] {99, 117, -1, -1}, 108),
        Arguments.of(new int[] {109, -1, -1, 108}, 117),
        Arguments.of(new int[] {110, 128, -1, 109}, 118),
        Arguments.of(new int[] {119, 137, -1, 118}, 128),
        Arguments.of(new int[] {137, 155, -1, -1}, 146),
        Arguments.of(new int[] {147, -1, -1, 146}, 155),
        Arguments.of(new int[] {148, -1, -1, 147}, 156),
        Arguments.of(new int[] {149, -1, -1, 148}, 157),
        Arguments.of(new int[] {150, -1, -1, 149}, 158),
        Arguments.of(new int[] {-1, -1, -1, 150}, 159),
        Arguments.of(new int[] {-1, -1, 150, 132}, 141),
        Arguments.of(new int[] {115, -1, 132, 114}, 123),
        Arguments.of(new int[] {116, -1, -1, 115}, 124),
        Arguments.of(new int[] {-1, -1, -1, 116}, 125),
        Arguments.of(new int[] {-1, -1, 116, 98}, 107),
        Arguments.of(new int[] {-1, -1, 98, 80}, 89),
        Arguments.of(new int[] {-1, -1, 80, 62}, 71),
        Arguments.of(new int[] {-1, -1, 62, 44}, 53),
        Arguments.of(new int[] {-1, 53, 52, -1}, 44),
        Arguments.of(new int[] {-1, 52, 51, 33}, 43));
  }

  @ParameterizedTest
  @MethodSource("getIndexSource")
  void getIndexInDirectionFrom(int[] expected, int index) {
    for (int i = 0; i < Direction.values().length; i++) {
      Assertions.assertEquals(
          expected[i],
          Direction.values()[i].getIndexInDirectionFrom(index),
          Direction.values()[i].name());
    }
  }
}
