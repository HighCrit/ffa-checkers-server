package com.highcrit.ffacheckers.socket.game.objects.moves;

import java.util.Objects;

import com.highcrit.ffacheckers.socket.game.objects.Piece;

public class Move {
  private static final String FEN_CAPTURE_SYMBOL = "x";
  private static final String FEN_MOVE_SYMBOL = "-";

  private int start;
  private int end;
  private Piece takes;

  public Move() {}

  public Move(int start, int end, Piece takes) {
    this.start = start;
    this.end = end;
    this.takes = takes;
  }

  public Move(int start, int end) {
    this(start, end, null);
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public Piece getTakes() {
    return takes;
  }

  @Override
  public String toString() {
    if (takes == null) {
      return start + FEN_MOVE_SYMBOL + end;
    }
    return start + FEN_CAPTURE_SYMBOL + end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Move move = (Move) o;
    return start == move.start && end == move.end && Objects.equals(takes, move.takes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }
}
