package com.highcrit.ffacheckers.socket.game.objects.moves;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.highcrit.ffacheckers.domain.entities.Move;
import lombok.Getter;

public class MoveSequence {
  @Getter private final LinkedList<Move> sequence;

  public MoveSequence(List<Move> sequence) {
    this.sequence = new LinkedList<>(sequence);
  }

  public MoveSequence() {
    this.sequence = new LinkedList<>();
  }

  public void addMove(Move move) {
    this.sequence.add(move);
  }

  public void undoMove() {
    this.sequence.removeLast();
  }

  public int length() {
    return sequence.size();
  }

  @Override
  public String toString() {
    if (sequence.isEmpty()) {
      return "";
    }
    return sequence.get(0).getStart()
        + "x"
        + sequence.stream().map(m -> String.valueOf(m.getEnd())).collect(Collectors.joining("x"));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MoveSequence that = (MoveSequence) o;
    return sequence.equals(that.sequence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sequence);
  }
}
