package com.highcrit.ffacheckers.domain.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;

@Entity
public class Piece {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private PlayerColor playerColor;
  private int position;
  private boolean isKing;

  private transient boolean isGhost = false;

  public Piece() {
  }

  public Piece(PlayerColor playerColor, int position, boolean isKing) {
    this.playerColor = playerColor;
    this.position = position;
    this.isKing = isKing;
  }

  public Piece(PlayerColor playerColor, int position) {
    this(playerColor, position, false);
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public void makeKing() {
    this.isKing = true;
  }

  public boolean isKing() {
    return isKing;
  }

  public boolean isGhost() {
    return isGhost;
  }

  public void setGhost(boolean ghost) {
    isGhost = ghost;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public String toString() {
    return (isKing ? "K" : "") + this.position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Piece piece = (Piece) o;
    return position == piece.position && isKing == piece.isKing && playerColor == piece.playerColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerColor, position, isKing);
  }
}
