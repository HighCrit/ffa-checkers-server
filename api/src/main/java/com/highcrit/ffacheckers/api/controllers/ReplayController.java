package com.highcrit.ffacheckers.api.controllers;

import java.util.UUID;

import com.highcrit.ffacheckers.api.services.ReplayService;
import com.highcrit.ffacheckers.domain.entities.Replay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/replay")
public class ReplayController {
  private final ReplayService replayService;

  @Autowired
  public ReplayController(ReplayService replayService) {
    this.replayService = replayService;
  }

  @GetMapping("/")
  public Page<Replay> getAllReplays(@RequestParam int page) {
    return replayService.getAll(page);
  }

  @GetMapping("/{id}")
  public Replay getReplayById(@PathVariable("id") UUID id) {
    return replayService.get(id);
  }

  @PostMapping("/")
  public ResponseEntity<Replay> saveReplay(@RequestBody Replay replay) {
    boolean success = replayService.saveReplay(replay);
    if (success) {
      return ResponseEntity.status(HttpStatus.CREATED).body(replay);
    }
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(replay);
  }

  @DeleteMapping("/{id}")
  public void deleteReplay(@PathVariable("id") UUID id) {
    replayService.deleteReplay(id);
  }
}
