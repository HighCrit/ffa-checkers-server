package com.highcrit.ffacheckers.socket.game.enums;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import lombok.Getter;

public enum GameEvent implements Event {
  /* Inbound */
  MOVE_ACTION("game-move-action"),

  /* Outbound */
  MOVE_RESULT("game-move-result"),
  MOVE_SET("game-move-set"),
  CURRENT_PLAYER("game-current-player"),
  YOUR_COLOR("game-your-color"),
  BOARD("game-board"),
  STATE("game-state"),
  WON("game-won-by");

  @Getter private final String eventName;

  GameEvent(String eventName) {
    this.eventName = eventName;
  }
}
