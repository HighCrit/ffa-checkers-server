package com.highcrit.ffacheckers.socket.lobby.objects;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.socket.game.objects.Game;
import com.highcrit.ffacheckers.socket.server.objects.clients.AbstractClient;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;

public interface ILobby {
  Game getGame();

  UUID getCode();

  void onPlayerLoaded(AbstractClient info);

  void onPlayerDisconnect(AbstractClient info);

  void onPlayerReconnect(PlayerClient info);

  void onGameEnd();

  Result addPlayer(AbstractClient client);

  void removePlayer(PlayerClient info);

  void send(Event event, Object data);
}
