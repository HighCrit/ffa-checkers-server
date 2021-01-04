package com.highcrit.ffacheckers.api.repositories;

import com.highcrit.ffacheckers.domain.entities.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
}
