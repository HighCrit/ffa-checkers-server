package com.highcrit.ffacheckers.socket.lobby.objects.data;

import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyAddAIResult implements Result {
  private PlayerColor playerColor;

  @Override
  public boolean isSuccess() {
    return true;
  }
}
