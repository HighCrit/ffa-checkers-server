package com.highcrit.ffacheckers.api.services;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.entities.Replay;
import com.highcrit.ffacheckers.api.repositories.ReplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReplayService {
    private final ReplayRepository replayRepository;

    @Autowired
    public ReplayService(ReplayRepository replayRepository) {
        this.replayRepository = replayRepository;
    }

    public Replay get(UUID id) {
        return replayRepository.findById(id).orElse(null);
    }

    public Page<Replay> getAll(int page) {
        return replayRepository.findAll(PageRequest.of(page, 5));
    }

    public boolean saveReplay(Replay replay) {
        if (replay.getId() == null || replay.getMoves() == null || replay.getMoves().isEmpty()) {
            return false;
        }
        replayRepository.save(replay);
        return true;
    }

    public void deleteReplay(UUID id) {
        replayRepository.deleteById(id);
    }
}
