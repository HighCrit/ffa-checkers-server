package com.highcrit.ffacheckers.domain.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Replay {
  @Id
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  @Column(length = 650)
  private String initialFen;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
  private List<Move> moves;
}
