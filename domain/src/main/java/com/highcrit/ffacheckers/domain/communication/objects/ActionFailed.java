package com.highcrit.ffacheckers.domain.communication.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class ActionFailed implements Result {
  @Getter private final String message;

  @Override
  public boolean isSuccess() {
    return false;
  }
}
