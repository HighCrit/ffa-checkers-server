package com.highcrit.ffacheckers.socket.game.enums;

public enum Direction {
  UP_RIGHT("DOWN_LEFT", -9),
  DOWN_RIGHT("UP_LEFT", 9),
  DOWN_LEFT("UP_RIGHT", 8),
  UP_LEFT("DOWN_RIGHT", -10);

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
