package com.highcrit.ffacheckers.socket.lobby.objects.data;

import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;

public class LobbyAddAIResult implements Result {
  private PlayerColor playerColor;

  public LobbyAddAIResult(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }
}
