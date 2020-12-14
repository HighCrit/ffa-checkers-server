package com.highcrit.ffacheckers.socket.lobby.objects.data;

import java.util.UUID;

import com.highcrit.ffacheckers.socket.utils.data.Result;

public class LobbyJoinResult implements Result {
  private final UUID code;

  public LobbyJoinResult(UUID code) {
    this.code = code;
  }

  public UUID getCode() {
    return code;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }
}
