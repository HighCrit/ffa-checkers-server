package com.highcrit.ffacheckers.socket.lobby.objects.data;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyJoinResult implements Result {
  private UUID code;

  @Override
  public boolean isSuccess() {
    return true;
  }
}
