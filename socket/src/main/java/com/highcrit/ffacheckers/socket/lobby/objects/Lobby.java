package com.highcrit.ffacheckers.socket.lobby.objects;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.objects.Game;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinResult;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyPlayers;
import com.highcrit.ffacheckers.socket.server.objects.AIClient;
import com.highcrit.ffacheckers.socket.server.objects.AbstractClient;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.socket.utils.TaskScheduler;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lobby {
  private static final Logger LOGGER = LoggerFactory.getLogger(Lobby.class);
  private static final int LOBBY_MAX_IDLE_TIME = 120 * 60; // 2 hours
  private static final TaskScheduler scheduler = new TaskScheduler();

  @Getter(AccessLevel.PACKAGE)
  private final HashMap<UUID, AbstractClient> connectedClients = new HashMap<>();

  @Getter private final LobbyManager lobbyManager;
  @Getter private final Game game = new Game(this);
  @Getter private final UUID code = UUID.randomUUID();

  public Lobby(LobbyManager lobbyManager) {
    this.lobbyManager = lobbyManager;
    scheduler.scheduleTask(() -> lobbyManager.delete(code, "Inactivity"), LOBBY_MAX_IDLE_TIME);
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
    send(LobbyEvent.PLAYER_DISCONNECT, info.getPlayerColor());
  }

  public void onPlayerReconnect(PlayerClient info) {
    game.onPlayerLoaded(info);
    info.send(LobbyEvent.PLAYER_RECONNECT, new LobbyJoinResult(code));

    info.setLobby(this);
    sendPlayers();
  }

  public Result addPlayer(AbstractClient client) {
    if (!connectedClients.containsKey(client.getId())) {
      connectedClients.put(client.getId(), client);
      game.addPlayer(client);
      client.setLobby(this);
    }

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

  private void sendPlayers() {
    Map<PlayerColor, String> players = new EnumMap<>(PlayerColor.class);
    game.getPlayers().forEach((color, player) -> players.put(color, player.getName()));
    send(LobbyEvent.PLAYER_JOINED, new LobbyPlayers(players));
  }
}
