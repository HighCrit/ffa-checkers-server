package com.highcrit.ffacheckers.socket.lobby.objects;

import java.util.HashMap;
import java.util.UUID;

import com.highcrit.ffacheckers.socket.game.objects.Game;
import com.highcrit.ffacheckers.socket.server.objects.AbstractClient;
import com.highcrit.ffacheckers.socket.lobby.instances.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinResult;
import com.highcrit.ffacheckers.socket.server.enums.ConnectionState;
import com.highcrit.ffacheckers.socket.server.instances.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.socket.utils.TaskScheduler;
import com.highcrit.ffacheckers.socket.utils.data.ActionFailed;
import com.highcrit.ffacheckers.socket.utils.data.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lobby {
  private static final Logger LOGGER = LoggerFactory.getLogger(Lobby.class);
  private static final int LOBBY_MAX_IDLE_TIME = 10 * 60; // 10 minutes
  private static final TaskScheduler scheduler = new TaskScheduler();

  private final HashMap<UUID, AbstractClient> connectedClients = new HashMap<>();

  private final LobbyManager lobbyManager;
  private final Game game = new Game(this);
  private final String code;

  public Lobby(LobbyManager lobbyManager, String code) {
    this.lobbyManager = lobbyManager;
    this.code = code;

    scheduler.scheduleTask(
        () -> {
          if (!this.game.hasStarted()) {
            lobbyManager.delete(code, "Inactivity");
          }
        },
        LOBBY_MAX_IDLE_TIME);
  }

  public void onPlayerDisconnect(AbstractClient info) {
    if (connectedClients.get(info.getId()) == null) {
      LOGGER.error("Tried to handle player disconnect that wasn't in this lobby");
      return;
    }
    pause();
  }

  public void onPlayerReconnect(PlayerClient info) {
      game.onPlayerLoaded(info);
      info.send("lobby-reconnect", new LobbyJoinResult(code));

      if (shouldResume()) {
          unpause();
      }

      info.setLobby(this);
  }

  public Result addPlayer(UUID id, AbstractClient client) {
      if (connectedClients.size() >= Game.MAX_PLAYERS) {
          return new ActionFailed("Lobby is full");
      }
    connectedClients.put(id, client);
    return new LobbyJoinResult(code);
  }

  public void removePlayer(PlayerClient info) {
      connectedClients.remove(info.getId());
      game.removePlayer(info);
      send("player-left", null);
  }

  public void delete() {
      connectedClients.values().forEach(s -> s.setLobby(null));
  }

  public void send(String eventName, Object data) {
      connectedClients.values().forEach(s -> s.send(eventName, data));
  }


  public void pause() {
      send("game-pause", null);
  }

  public void unpause() {
      send("game-unpaused", null);
  }

  // TODO:
  public boolean shouldResume() {
      return true;
  }

    public String getCode() {
        return code;
    }
}
