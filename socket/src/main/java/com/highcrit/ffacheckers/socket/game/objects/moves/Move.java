package com.highcrit.ffacheckers.socket.game.objects.moves;

public abstract class Move {
  protected static final String FEN_CAPTURE_SYMBOL = "x";
  protected static final String FEN_MOVE_SYMBOL = "-";

  protected final int start;
  protected int end;

  public Move(int start) {
    this.start = start;
    this.end = start;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  @Override
  public abstract String toString();

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();
}
