package com.highcrit.ffacheckers.socket.server.objects.clients;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.lobby.objects.ILobby;
import lombok.Data;

@Data
public abstract class AbstractClient {
  protected final UUID id;
  protected String name;
  protected PlayerColor playerColor;
  private ILobby lobby;
  private boolean loaded;
  private boolean gone;

  public AbstractClient(UUID id) {
    this.id = id;
  }

  public abstract void send(Event event, Object data);

  public abstract boolean isBot();
}
