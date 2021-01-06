package com.highcrit.ffacheckers.socket.server.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.enums.SocketEvent;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;

public class OnResetUUID implements DataListener<UID> {
  private final SocketManager socketManager;

  public OnResetUUID(SocketManager socketManager) {
    this.socketManager = socketManager;
  }

  @Override
  public void onData(SocketIOClient socketIOClient, UID uid, AckRequest ackRequest) {
    PlayerClient info = socketManager.getById(uid.getId());
    if (info == null) {
      return;
    }
    if (info.getLobby() != null) {
      info.getLobby().removePlayer(info);
    }

    this.socketManager.remove(info.getId());
    UID id = this.socketManager.registerClient(socketIOClient, null);
    info.send(SocketEvent.UUID, id);
  }
}
