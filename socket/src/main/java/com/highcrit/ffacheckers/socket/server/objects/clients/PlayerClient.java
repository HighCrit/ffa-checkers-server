package com.highcrit.ffacheckers.socket.server.objects.clients;

import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.server.enums.ConnectionState;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerClient extends AbstractClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerClient.class);

  @Getter @Setter private ConnectionState state = ConnectionState.CONNECTED;
  @Getter @Setter private boolean isHost = false;
  @Getter @Setter private SocketIOClient socket;

  public PlayerClient(UUID id, SocketIOClient socket) {
    super(id);
    this.socket = socket;
  }

  @Override
  public void send(Event event, Object data) {
    LOGGER.info(
        String.format(
            "Sending event \"%s\" to socket id: %s data: %s",
            event.getEventName(), id, data.toString()));
    socket.sendEvent(event.getEventName(), data);
  }

  @Override
  public void setPlayerColor(PlayerColor playerColor) {
    super.setPlayerColor(playerColor);
    setName(playerColor.name());
  }

  @Override
  public boolean isBot() {
    return false;
  }
}
