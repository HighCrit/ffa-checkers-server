package com.highcrit.ffacheckers.socket.server;

import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;

public interface ISocketManager {
  /**
   * Makes the server listen to a specific event
   * @param event event enum instance
   * @param dto class used for the exchange of information
   * @param listener class that listens for the event
   * @param <T> type of dto
   */
  <T> void addEventListener(Event event, Class<T> dto, DataListener<T> listener);

  /**
   * Retrieves the client info by using the current socket instance
   * @param socketIOClient socket instance
   * @return client info
   */
  PlayerClient getInfoByClient(SocketIOClient socketIOClient);

  /**
   * Retrieves the client info by using it's unique id
   * @param id unique id
   * @return client info
   */
  PlayerClient getById(UUID id);

  /**
   * Registers a client to the socket manager
   * @param socket socket instance
   * @param knownId know unique id (null if unknown)
   * @return a unique id dto
   */
  UID registerClient(SocketIOClient socket, UUID knownId);

  /**
   * Removes a client from the socket manager by it's id
   * @param id unique id
   */
  void remove(UUID id);
}
