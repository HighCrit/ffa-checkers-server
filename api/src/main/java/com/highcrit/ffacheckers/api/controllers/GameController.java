package com.highcrit.ffacheckers.api.controllers;

import java.util.UUID;

import com.highcrit.ffacheckers.api.services.GameService;
import com.highcrit.ffacheckers.domain.entities.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/game")
public class GameController {
  private final GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("/")
  public @ResponseBody Page<Game> getAll(@RequestParam int page) {
    return gameService.getAll(page);
  }

  @GetMapping("/{id}")
  public @ResponseBody Game get(@PathVariable("id") UUID id) {
    return gameService.get(id);
  }
}
