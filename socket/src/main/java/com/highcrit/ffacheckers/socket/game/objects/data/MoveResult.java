package com.highcrit.ffacheckers.socket.game.objects.data;

import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.domain.entities.Move;

public class MoveResult implements Result {
  private Move move;

  public MoveResult(Move move) {
    this.move = move;
  }

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
  }

  @Override
  public boolean isSuccess() {
    return true;
  }
}
