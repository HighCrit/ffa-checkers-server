package com.highcrit.ffacheckers.api.controllers;

import com.highcrit.ffacheckers.api.repositories.ReplayRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ReplayController {
  protected final ReplayRepository replayRepository;
}
