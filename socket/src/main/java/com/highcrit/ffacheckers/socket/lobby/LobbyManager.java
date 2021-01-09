package com.highcrit.ffacheckers.socket.lobby;

import java.util.HashMap;
import java.util.UUID;

import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyClosing;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinAction;
import com.highcrit.ffacheckers.socket.lobby.objects.listeners.OnLobbyAddAI;
import com.highcrit.ffacheckers.socket.lobby.objects.listeners.OnLobbyCreate;
import com.highcrit.ffacheckers.socket.lobby.objects.listeners.OnLobbyJoin;
import com.highcrit.ffacheckers.socket.lobby.objects.listeners.OnLobbyLeave;
import com.highcrit.ffacheckers.socket.lobby.objects.listeners.OnLobbyLoaded;
import com.highcrit.ffacheckers.socket.server.ISocketManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobbyManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(LobbyManager.class);

  private final ISocketManager socketManager;

  @Getter(AccessLevel.PACKAGE)
  private final HashMap<UUID, Lobby> lobbies = new HashMap<>();

  public LobbyManager(ISocketManager socketManager) {
    this.socketManager = socketManager;
  }

  /**
   * Registers listeners to the socket server for lobby related events
   */
  public void initialize() {
    socketManager.addEventListener(
        LobbyEvent.CREATE_ACTION, null, new OnLobbyCreate(this, socketManager));
    socketManager.addEventListener(
        LobbyEvent.JOIN_ACTION, LobbyJoinAction.class, new OnLobbyJoin(this, socketManager));
    socketManager.addEventListener(LobbyEvent.LEAVE, null, new OnLobbyLeave(socketManager));
    socketManager.addEventListener(LobbyEvent.ADD_AI_ACTION, null, new OnLobbyAddAI(socketManager));
    socketManager.addEventListener(LobbyEvent.LOADED, null, new OnLobbyLoaded(socketManager));
  }

  /**
   * Creates a lobby and saves it
   * @return lobby instance
   */
  public Lobby create() {
    final Lobby lobby = new Lobby(this);
    this.lobbies.put(lobby.getCode(), lobby);

    return lobby;
  }

  /**
   * Removes a lobby for given id
   * @param code lobby id
   * @param reason reason for deleting
   */
  public void delete(UUID code, String reason) {
    Lobby lobby = lobbies.get(code);
    if (lobby == null) {
      LOGGER.error(String.format("Failed to close lobby (%s) with reason: %s", code, reason));
      return;
    }
    LOGGER.info(String.format("Closing lobby (%s) with reason: %s", code, reason));
    lobby.send(LobbyEvent.CLOSING, new LobbyClosing(reason));
    lobby.delete();
    lobbies.remove(code);
  }

  /**
   * Returns whether a lobby exists for the given id
   * @param code lobby id
   * @return true if lobby exists
   */
  public boolean has(UUID code) {
    return lobbies.containsKey(code);
  }

  /**
   * Returns a lobby for given id
   * @param code lobby id
   * @return lobby instance or null
   */
  public Lobby get(UUID code) {
    return lobbies.get(code);
  }
}
