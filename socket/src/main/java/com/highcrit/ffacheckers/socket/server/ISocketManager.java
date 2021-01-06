package com.highcrit.ffacheckers.socket.server;

import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.domain.communication.objects.Event;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;

public interface ISocketManager {
  <T> void addEventListener(Event event, Class<T> dto, DataListener<T> listener);

  PlayerClient getInfoByClient(SocketIOClient socketIOClient);

  PlayerClient getById(UUID id);

  UID registerClient(SocketIOClient socket, UUID knownId);

  void remove(UUID id);
}
