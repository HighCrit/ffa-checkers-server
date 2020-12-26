package com.highcrit.ffacheckers.api.controllers;

import com.highcrit.ffacheckers.api.repositories.ReplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/replay")
public class ReplayController {
  protected final ReplayRepository replayRepository;

  @Autowired
  public ReplayController(ReplayRepository replayRepository) {
    this.replayRepository = replayRepository;
  }
}
