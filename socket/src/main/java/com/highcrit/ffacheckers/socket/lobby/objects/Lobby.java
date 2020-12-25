package com.highcrit.ffacheckers.socket.lobby.objects;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.objects.Game;
import com.highcrit.ffacheckers.socket.lobby.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinResult;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyPlayers;
import com.highcrit.ffacheckers.socket.server.objects.AIClient;
import com.highcrit.ffacheckers.socket.server.objects.AbstractClient;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.socket.utils.TaskScheduler;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.domain.communication.objects.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lobby {
  private static final Logger LOGGER = LoggerFactory.getLogger(Lobby.class);
  private static final int LOBBY_MAX_IDLE_TIME = 30 * 60; // 10 minutes
  private static final TaskScheduler scheduler = new TaskScheduler();

  private final HashMap<UUID, AbstractClient> connectedClients = new HashMap<>();
  private final LobbyManager lobbyManager;

  private final Game game = new Game(this);
  private final UUID code = UUID.randomUUID();

  public Lobby(LobbyManager lobbyManager) {
    this.lobbyManager = lobbyManager;
    scheduler.scheduleTask(
        () -> {
          if (!this.game.hasStarted()) {
            lobbyManager.delete(code, "Inactivity");
          }
        },
        LOBBY_MAX_IDLE_TIME);
  }

  public void onPlayerLoaded(AbstractClient info) {
    sendPlayers();
    game.onPlayerLoaded(info);
  }

  public void onPlayerDisconnect(AbstractClient info) {
    if (connectedClients.get(info.getId()) == null) {
      LOGGER.error("Tried to handle player disconnect that wasn't in this lobby");
      return;
    }
    // TODO: Send info on disconnected player
    send(LobbyEvent.PLAYER_DISCONNECT, null);
  }

  public void onPlayerReconnect(PlayerClient info) {
    game.onPlayerLoaded(info);
    info.send(LobbyEvent.PLAYER_RECONNECT, new LobbyJoinResult(code));

    info.setLobby(this);
    sendPlayers();
  }

  public Result addPlayer(UUID id, AbstractClient client) {
    if (connectedClients.size() >= Game.MAX_PLAYERS) {
      return new ActionFailed("Lobby is full");
    }
    connectedClients.put(id, client);
    game.addPlayer(client);
    client.setLobby(this);

    sendPlayers();

    return new LobbyJoinResult(code);
  }

  public void removePlayer(PlayerClient info) {
    connectedClients.remove(info.getId());
    if (connectedClients.values().stream().allMatch(c -> c instanceof AIClient)) {
      lobbyManager.delete(code, "No players left");
    } else {
      game.removePlayer(info);
      info.setHost(false);
      sendPlayers();
    }
  }

  public void delete() {
    connectedClients.values().forEach(s -> s.setLobby(null));
  }

  public void send(Event event, Object data) {
    connectedClients.values().forEach(s -> s.send(event, data));
  }

  public UUID getCode() {
    return code;
  }

  public Game getGame() {
    return game;
  }

  private void sendPlayers() {
    Map<PlayerColor, String> players = new EnumMap<>(PlayerColor.class);
    game.getPlayers().forEach((color, player) -> players.put(color, player.getName()));
    send(LobbyEvent.PLAYER_JOINED, new LobbyPlayers(players));
  }
}
