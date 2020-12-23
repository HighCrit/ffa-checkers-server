package com.highcrit.ffacheckers.domain.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game {
    @Id
    private UUID id;
    @OneToMany
    private List<Move> moves;

    public Game(UUID id, List<Move> moves) {
        this.id = id;
        this.moves = moves;
    }

    public Game() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
