package com.highcrit.ffacheckers.socket.lobby;

import java.util.HashMap;
import java.util.UUID;

import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyClosing;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobbyManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(LobbyManager.class);

  private final SocketManager socketManager;

  @Getter(AccessLevel.PACKAGE)
  private final HashMap<UUID, Lobby> lobbies = new HashMap<>();

  public LobbyManager(SocketManager socketManager) {
    this.socketManager = socketManager;
  }

  public Lobby create() {
    final Lobby lobby = new Lobby(this);
    this.lobbies.put(lobby.getCode(), lobby);

    return lobby;
  }

  public void delete(UUID code, String reason) {
    Lobby lobby = lobbies.get(code);
    if (lobby == null) {
      return;
    }
    LOGGER.info(String.format("Closing lobby (%s) with reason: %s", code, reason));
    lobby.send(LobbyEvent.CLOSING, new LobbyClosing(reason));
    lobby.delete();
    socketManager.clearRoom(code);
    lobbies.remove(code);
  }

  public boolean has(UUID code) {
    return lobbies.containsKey(code);
  }

  public Lobby get(UUID code) {
    return lobbies.get(code);
  }
}
