package com.highcrit.ffacheckers.socket.server.objects.data;

import java.util.UUID;

public class UID {
  private UUID id;

  public UID() {}

  public UID(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
