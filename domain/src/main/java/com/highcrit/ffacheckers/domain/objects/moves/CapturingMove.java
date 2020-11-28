package com.highcrit.ffacheckers.domain.objects.moves;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.highcrit.ffacheckers.domain.enums.Direction;

public class CapturingMove extends Move {
  private final List<Direction> jumps = new ArrayList<>();

  public CapturingMove(int start) {
    super(start);
  }

  public CapturingMove(int start, List<Direction> jumps) {
    super(start);
    jumps.forEach(this::addJump);
  }

  public void addJump(Direction jumpDir) {
    this.jumps.add(jumpDir);
    this.end = jumpDir.getIndexInDirectionFrom(jumpDir.getIndexInDirectionFrom(this.end));
  }

  public void undoJump() {
    if (this.jumps.size() > 1) {
      int lastIndex = this.jumps.size() - 1;
      Direction opposite = this.jumps.get(lastIndex).getOppositeDirection();
      this.end = opposite.getIndexInDirectionFrom(opposite.getIndexInDirectionFrom(end));
      this.jumps.remove(lastIndex);
    }
  }

  @Override
  public CapturingMove copy() {
    return new CapturingMove(start, this.jumps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(start);
    int lastIndex = start;
    for (Direction dir : jumps) {
      sb.append(FEN_CAPTURE_SYMBOL);
      lastIndex = dir.getIndexInDirectionFrom(dir.getIndexInDirectionFrom(lastIndex));
      sb.append(lastIndex);
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CapturingMove that = (CapturingMove) o;
    return start == that.start && end == that.end && jumps.equals(that.jumps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, jumps);
  }
}
