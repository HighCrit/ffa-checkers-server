package com.highcrit.ffacheckers.domain.enums;

public enum PlayerColor {
  YELLOW('Y'),
  BLUE('B'),
  GREEN('G'),
  RED('R');

  private final char colorChar;

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

  public char getColorChar() {
    return colorChar;
  }
}
