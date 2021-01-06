package com.highcrit.ffacheckers.socket.server.objects.clients;

import java.util.List;
import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.socket.game.enums.GameEvent;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIClient extends AbstractClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(AIClient.class);

  public AIClient() {
    super(UUID.randomUUID());
    setName("BOT 🤖");
  }

  @Override
  @SuppressWarnings("unchecked")
  public void send(Event event, Object data) {
    if (LobbyEvent.PLAYER_JOINED.equals(event)) {
      LOGGER.info(String.format("%s bot received event \"%s\"", playerColor, event.getEventName()));
      if (!isLoaded()) {
        getLobby().getGame().onPlayerLoaded(this);
      }
    } else if (GameEvent.MOVE_SET.equals(event)) {
      LOGGER.info(String.format("%s bot received event \"%s\"", playerColor, event.getEventName()));
      getLobby().getGame().onMove(this, ((List<Move>) data).get(0));
    }
  }
}
