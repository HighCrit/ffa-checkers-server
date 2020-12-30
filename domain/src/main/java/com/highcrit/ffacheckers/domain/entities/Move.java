package com.highcrit.ffacheckers.domain.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Move {
  private static final String FEN_CAPTURE_SYMBOL = "x";
  private static final String FEN_MOVE_SYMBOL = "-";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private int start;
  private int end;
  private boolean promoting;

  @OneToOne(cascade = {CascadeType.ALL})
  private Piece takes;

  public Move() {}

  public Move(int start, int end, boolean promoting, Piece takes) {
    this.start = start;
    this.end = end;
    this.promoting = promoting;
    this.takes = takes;
  }

  public Move(int start, int end, Piece takes) {
    this(start, end, false, takes);
  }

  public Move(int start, int end, boolean promoting) {
    this(start, end, promoting, null);
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public boolean isPromoting() {
    return promoting;
  }

  public void setPromoting(boolean promoting) {
    this.promoting = promoting;
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
    return start == move.start
        && end == move.end
        && promoting == move.promoting
        && Objects.equals(takes, move.takes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }
}
