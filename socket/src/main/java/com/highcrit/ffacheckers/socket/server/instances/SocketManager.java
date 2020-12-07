package com.highcrit.ffacheckers.socket.server.instances;

import java.util.HashMap;
import java.util.UUID;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.server.enums.ConnectionState;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;
import com.highcrit.ffacheckers.socket.server.objects.listeners.OnConnection;
import com.highcrit.ffacheckers.socket.server.objects.listeners.OnUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(SocketManager.class);

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
    addEventListener("uuid", UID.class, new OnUUID(this));
  }

  public <T> void addEventListener(String eventName, Class<T> dto, DataListener<T> listener) {
    LOGGER.info(String.format("Registering listener for event: %s", eventName));
    server.addEventListener(eventName, dto, listener);
  }

  public boolean has(UUID id) {
    return sockets.containsKey(id);
  }

  public void remove(UUID id) {
    LOGGER.info(String.format("Removing socket with id: %s", id));
    sockets.remove(id);
  }

  public void joinRoom(UUID id, String room) {
    if (!has(id)) {
      return;
    }
    LOGGER.info(String.format("Moving socket with id: %s to lobby: %s", id, room));
    sockets.get(id).getSocket().joinRoom(room);
  }

  public void clearRoom(String code) {
    LOGGER.info(String.format("Removing sockets from lobby: %s", code));
    BroadcastOperations room = server.getRoomOperations(code);
    if (room != null) room.disconnect();
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
        joinRoom(info.getId(), info.getLobby().getCode());
        info.getLobby().onPlayerReconnect(info);
      }
    } else {
      LOGGER.info(String.format("Received new connection from socket: %s", knownId));
      sockets.put(knownId, new PlayerClient(knownId, socket));
    }

    return new UID(knownId);
  }
}
