package com.highcrit.ffacheckers.domain.communication.objects;

public class ActionFailed implements Result {
  private final String message;

  public ActionFailed(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean isSuccess() {
    return false;
  }
}
