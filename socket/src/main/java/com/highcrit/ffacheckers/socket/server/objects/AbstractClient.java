package com.highcrit.ffacheckers.socket.server.objects;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.enums.ConnectionState;

public abstract class AbstractClient {
  private ConnectionState state = ConnectionState.CONNECTED;
  private Lobby lobby;

  protected final UUID id;
  protected String name;
  protected PlayerColor playerColor;
  private boolean loaded;
  private boolean left;

  public AbstractClient(UUID id) {
    this.id = id;
  }

  public abstract void send(String eventName, Object data);

  public ConnectionState getState() {
    return state;
  }

  public void setState(ConnectionState state) {
    this.state = state;
  }

  public Lobby getLobby() {
    return lobby;
  }

  public void setLobby(Lobby lobby) {
    this.lobby = lobby;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public boolean isLoaded() {
    return loaded;
  }

  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }

  public boolean hasLeft() {
    return left;
  }

  public void setLeft(boolean left) {
    this.left = left;
  }
}
