package com.highcrit.ffacheckers.domain.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
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

  @Override
  public String toString() {
    if (takes == null) {
      return start + FEN_MOVE_SYMBOL + end;
    }
    return start + FEN_CAPTURE_SYMBOL + end;
  }
}
