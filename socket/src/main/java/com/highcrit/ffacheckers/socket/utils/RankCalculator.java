package com.highcrit.ffacheckers.socket.utils;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RankCalculator {
  /**
   * Returns the rank a.k.a. row from the player's home base perspective using the index
   * @param color color of player
   * @param index position on the bord according to the FEN specifications
   * @return rank of the index for the player color
   */
  public int getRankFromIndexForColor(PlayerColor color, int index) {
    int row = index / 9;
    int col = (index % 9) * 2 + (row % 2);
    switch (color) {
      case YELLOW:
        return 18 - row;
      case RED:
        return 18 - col;
      case BLUE:
        return col + 1;
      case GREEN:
        return row + 1;
      default:
        throw new IllegalArgumentException("Invalid color");
    }
  }
}
