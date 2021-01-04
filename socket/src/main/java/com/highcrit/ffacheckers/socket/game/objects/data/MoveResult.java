package com.highcrit.ffacheckers.socket.game.objects.data;

import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.domain.entities.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveResult implements Result {
  private Move move;

  @Override
  public boolean isSuccess() {
    return true;
  }
}
