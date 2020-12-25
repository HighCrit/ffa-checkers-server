package com.highcrit.ffacheckers.socket.server.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.enums.SocketEvent;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;

public class OnUUID implements DataListener<UID> {
  private final SocketManager socketManager;

  public OnUUID(SocketManager socketManager) {
    this.socketManager = socketManager;
  }

  @Override
  public void onData(SocketIOClient socketIOClient, UID uid, AckRequest ackRequest) {
    UID id = socketManager.registerClient(socketIOClient, uid.getId());
    socketManager.getById(id.getId()).send(SocketEvent.UUID, id);
  }
}
