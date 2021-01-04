package com.highcrit.ffacheckers.domain.enums;

import lombok.Getter;

public enum PlayerColor {
  YELLOW('Y'),
  BLUE('B'),
  GREEN('G'),
  RED('R');

  @Getter private final char colorChar;

  PlayerColor(char colorChar) {
    this.colorChar = colorChar;
  }

  public static PlayerColor fromColorChar(char colorChar) {
    for (PlayerColor playerColor : PlayerColor.values()) {
      if (playerColor.colorChar == colorChar) {
        return playerColor;
      }
    }
    return null;
  }
}
