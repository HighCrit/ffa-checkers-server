package com.highcrit.ffacheckers.domain.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

@Entity
public class Replay {
  @Id
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @Column(length = 650)
  private String initialFen;

  @OneToMany(cascade = {CascadeType.ALL})
  private List<Move> moves;

  public Replay(UUID id, String initialFen, List<Move> moves) {
    this.id = id;
    this.initialFen = initialFen;
    this.moves = moves;
  }

  public Replay() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getInitialFen() {
    return initialFen;
  }

  public void setInitialFen(String initialFen) {
    this.initialFen = initialFen;
  }

  public List<Move> getMoves() {
    return moves;
  }

  public void setMoves(List<Move> moves) {
    this.moves = moves;
  }
}