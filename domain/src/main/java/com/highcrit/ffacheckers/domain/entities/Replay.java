package com.highcrit.ffacheckers.domain.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Replay {
    @Id
    private UUID id;
    @OneToMany
    private List<Move> moves;

    public Replay(UUID id, List<Move> moves) {
        this.id = id;
        this.moves = moves;
    }

    public Replay() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Move> getMoves() {
        return moves;
    }
}
