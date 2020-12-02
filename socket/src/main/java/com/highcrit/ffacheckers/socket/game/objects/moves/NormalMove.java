package com.highcrit.ffacheckers.socket.game.objects.moves;

import java.util.Objects;

import com.highcrit.ffacheckers.socket.game.enums.Direction;

public class NormalMove extends Move {
  public NormalMove(int start, Direction dir) {
    super(start);
    this.end = dir.getIndexInDirectionFrom(start);
  }

  @Override
  public String toString() {
    return this.start + FEN_MOVE_SYMBOL + this.end;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
      NormalMove that = (NormalMove) o;
      return start == that.start && end == that.end;
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }
}
