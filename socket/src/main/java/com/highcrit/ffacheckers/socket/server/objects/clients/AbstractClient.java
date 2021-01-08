package com.highcrit.ffacheckers.socket.server.objects.clients;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.lobby.objects.ILobby;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
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

  public void reset() {
    this.name = null;
    this.playerColor = null;
    this.lobby = null;
    this.loaded = false;
    this.gone = false;
  }
}
