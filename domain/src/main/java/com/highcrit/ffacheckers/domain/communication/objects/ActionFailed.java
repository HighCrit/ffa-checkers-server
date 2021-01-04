package com.highcrit.ffacheckers.domain.communication.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ActionFailed implements Result {
  @Getter private final String message;

  @Override
  public boolean isSuccess() {
    return false;
  }
}
