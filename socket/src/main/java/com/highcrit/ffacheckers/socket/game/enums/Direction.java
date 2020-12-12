package com.highcrit.ffacheckers.socket.game.enums;

import java.util.EnumMap;

public enum Direction {
  UP_RIGHT(-9),
  DOWN_RIGHT(9),
  DOWN_LEFT(8),
  UP_LEFT(-10);

  private final int indexOffset;

  Direction(int indexOffset) {
    this.indexOffset = indexOffset;
  }

  public int getIndexInDirectionFrom(int index) {
    if (index == -1) return -1;
    int rowOffset = (index / 9) % 2;
    int i = index + rowOffset + indexOffset;

    if (i < 2
        || i > 159
        || rowOffset == (i / 9) % 2
        || ((i / 9 < 4 || i / 9 > 13) && (i % 9 < 2 || i % 9 > 6))) {
      return -1;
    }

    return i;
  }
}
