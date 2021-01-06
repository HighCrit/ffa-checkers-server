package com.highcrit.ffacheckers.socket.server;

import java.util.HashMap;
import java.util.UUID;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.socket.game.enums.GameEvent;
import com.highcrit.ffacheckers.socket.game.objects.listeners.OnMove;
import com.highcrit.ffacheckers.socket.server.enums.ConnectionState;
import com.highcrit.ffacheckers.socket.server.enums.SocketEvent;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;
import com.highcrit.ffacheckers.socket.server.objects.listeners.OnConnection;
import com.highcrit.ffacheckers.socket.server.objects.listeners.OnDisconnection;
import com.highcrit.ffacheckers.socket.server.objects.listeners.OnUUID;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketManager implements ISocketManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(SocketManager.class);

  @Getter(AccessLevel.PACKAGE)
  private final HashMap<UUID, PlayerClient> sockets = new HashMap<>();

  private final SocketIOServer server;

  public SocketManager() {
    Configuration config = new Configuration();
    config.setContext("/sockets");
    config.setPort(6001);

    server = new SocketIOServer(config);
  }

  public void initialize() {
    LOGGER.info("Initializing SocketIOServer");
    registerEventListeners();
    LOGGER.info("Starting SocketIOServer");
    server.start();
  }

  private void registerEventListeners() {
    server.addConnectListener(new OnConnection());
    server.addDisconnectListener(new OnDisconnection(this));

    addEventListener(SocketEvent.UUID, UID.class, new OnUUID(this));
    addEventListener(GameEvent.MOVE_ACTION, Move.class, new OnMove(this));
  }

  public <T> void addEventListener(Event event, Class<T> dto, DataListener<T> listener) {
    LOGGER.info(String.format("Registering listener for event: %s", event));
    server.addEventListener(event.getEventName(), dto, listener);
  }

  public boolean has(UUID id) {
    return sockets.containsKey(id);
  }

  public void remove(UUID id) {
    LOGGER.info(String.format("Removing socket with id: %s", id));
    sockets.remove(id);
  }

  public PlayerClient getInfoByClient(SocketIOClient socketIOClient) {
    return sockets.values().stream()
        .filter(i -> i.getSocket().getSessionId() == socketIOClient.getSessionId())
        .findFirst()
        .orElse(null);
  }

  public PlayerClient getById(UUID id) {
    return sockets.get(id);
  }

  public UID registerClient(SocketIOClient socket, UUID knownId) {
    if (knownId == null) {
      knownId = UUID.randomUUID();
    }

    // If socket by id is known
    if (has(knownId)) {
      LOGGER.info(String.format("Re-established connections with socket: %s", knownId));
      PlayerClient info = this.sockets.get(knownId);
      info.setSocket(socket);
      info.setState(ConnectionState.CONNECTED);

      if (info.getLobby() != null) {
        // Reconnect
        info.getLobby().onPlayerReconnect(info);
      }
    } else {
      LOGGER.info(String.format("Received new connection from socket: %s", knownId));
      sockets.put(knownId, new PlayerClient(knownId, socket));
    }

    return new UID(knownId);
  }
}
