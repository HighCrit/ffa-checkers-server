package com.highcrit.ffacheckers.socket.server.enums;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import lombok.Getter;

public enum SocketEvent implements Event {
  /* Inbound */
  RESET_UUID("reset-uuid"),
  UUID("uuid"),

  /* Outbound */
  SEND_UUID("send-uuid");

  @Getter private final String eventName;

  SocketEvent(String eventName) {
    this.eventName = eventName;
  }
}
