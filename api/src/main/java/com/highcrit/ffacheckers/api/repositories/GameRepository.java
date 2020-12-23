package com.highcrit.ffacheckers.api.repositories;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.entities.Game;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends PagingAndSortingRepository<Game, UUID> {
}
