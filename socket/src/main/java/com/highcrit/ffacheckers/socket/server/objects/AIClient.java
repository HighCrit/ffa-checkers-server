package com.highcrit.ffacheckers.socket.server.objects;

import java.util.List;
import java.util.UUID;

import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.socket.game.objects.moves.MoveSequence;
import com.highcrit.ffacheckers.socket.utils.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIClient extends AbstractClient {
  private static final TaskScheduler SCHEDULER = new TaskScheduler();
  private static final Logger LOGGER = LoggerFactory.getLogger(AIClient.class);

  public AIClient() {
    super(UUID.randomUUID());
    setName("BOT");
  }

  @Override
  @SuppressWarnings("unchecked")
  public void send(String eventName, Object data) {
    switch (eventName) {
      case "lobby-player-joined":
        LOGGER.info(String.format("%s bot received event \"%s\"", playerColor, eventName));
        if (!isLoaded()) {
          getLobby().getGame().onPlayerLoaded(this);
        }
        break;
      case "game-move-set":
        // Add some time so the humans can follow our movements
        LOGGER.info(String.format("%s bot received event \"%s\"", playerColor, eventName));
        SCHEDULER.scheduleTask(() -> {
          if (((List<Object>) data).get(0) instanceof MoveSequence) {
            ((List<MoveSequence>) data).get(0).getSequence().forEach(m -> getLobby().getGame().onMove(this, m));
          } else {
            getLobby().getGame().onMove(this, ((List<Move>) data).get(0));
          }
        }, 1);
        break;
    }
  }
}
