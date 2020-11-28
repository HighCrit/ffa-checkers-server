package com.highcrit.ffacheckers.domain.enums;

public enum Direction {
  NORTHEAST("SOUTHWEST", -9),
  SOUTHEAST("NORTHWEST", 9),
  SOUTHWEST("NORTHEAST", 8),
  NORTHWEST("SOUTHEAST", -10);

  private final String oppositeDirection;
  private final int indexOffset;

  Direction(String oppositeDirection, int indexOffset) {
    this.oppositeDirection = oppositeDirection;
    this.indexOffset = indexOffset;
  }

  public Direction getOppositeDirection() {
    return Direction.valueOf(oppositeDirection);
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
