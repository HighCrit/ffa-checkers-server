package com.highcrit.ffacheckers.api.services;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.entities.Game;
import com.highcrit.ffacheckers.api.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game get(UUID id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Page<Game> getAll(int page) {
        return gameRepository.findAll(PageRequest.of(page, 5));
    }
}
