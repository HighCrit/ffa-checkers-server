package com.highcrit.ffacheckers.socket.lobby.objects.data;

import com.highcrit.ffacheckers.socket.utils.data.Result;

public class LobbyJoinResult implements Result {
  private final String code;

  public LobbyJoinResult(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }
}
