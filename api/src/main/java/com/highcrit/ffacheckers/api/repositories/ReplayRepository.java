package com.highcrit.ffacheckers.api.repositories;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.entities.Replay;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplayRepository extends PagingAndSortingRepository<Replay, UUID> {}
