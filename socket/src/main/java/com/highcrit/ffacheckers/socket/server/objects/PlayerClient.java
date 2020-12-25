package com.highcrit.ffacheckers.socket.server.objects;

import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerClient extends AbstractClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerClient.class);

  private boolean isHost = false;
  private SocketIOClient socket;

  public PlayerClient(UUID id, SocketIOClient socket) {
    super(id);
    this.socket = socket;
  }

  @Override
  public void send(Event event, Object data) {
    LOGGER.info(String.format("Sending event \"%s\" to socket id: %s", event.getEventName(), id));
    socket.sendEvent(event.getEventName(), data);
  }

  public boolean isHost() {
    return isHost;
  }

  public void setHost(boolean host) {
    isHost = host;
  }

  @Override
  public void setPlayerColor(PlayerColor playerColor) {
    super.setPlayerColor(playerColor);
    setName(playerColor.name());
  }

  public SocketIOClient getSocket() {
    return socket;
  }

  public void setSocket(SocketIOClient socket) {
    this.socket = socket;
  }
}
