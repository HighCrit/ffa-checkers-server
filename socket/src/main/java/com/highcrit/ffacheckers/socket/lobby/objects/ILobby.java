package com.highcrit.ffacheckers.socket.lobby.objects;

import java.util.UUID;

import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.socket.game.objects.Game;
import com.highcrit.ffacheckers.socket.server.objects.clients.AbstractClient;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;

public interface ILobby {
  /**
   * @return current game instance
   */
  Game getGame();

  /**
   * @return unique lobby code
   */
  UUID getCode();

  /**
   * This function is executed once a client is finished loading
   * @param info client info
   */
  void onPlayerLoaded(AbstractClient info);

  /**
   * This function is executed once the connection to a client is lost
   * @param info client info
   */
  void onPlayerDisconnect(AbstractClient info);

  /**
   * This function is executed once the connection to a client has been re-established
   * @param info client info
   */
  void onPlayerReconnect(PlayerClient info);

  /**
   * This function is executed once the game is over
   */
  void onGameEnd();

  /**
   * Adds a client to the lobby
   * @param client client info
   * @return result of adding a client
   */
  Result addPlayer(AbstractClient client);

  /**
   * Removes a client from the lobby
   * @param info client info
   */
  void removePlayer(PlayerClient info);

  /**
   * Sends an event to the entire lobby
   * @param event event
   * @param data corresponding data
   */
  void send(Event event, Object data);
}
