package com.highcrit.ffacheckers.socket.server.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.server.instances.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.AbstractClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;

public class OnResetUUID implements DataListener<UID> {
  private final SocketManager socketManager;

  public OnResetUUID(SocketManager socketManager) {
    this.socketManager = socketManager;
  }

  @Override
  public void onData(SocketIOClient socketIOClient, UID uid, AckRequest ackRequest) {
      AbstractClient info = socketManager.getById(uid.getId());
      if (info == null) {
          return;
      }
      if (info.getLobby() != null) {
          // TODO: Remove player from lobby
      }

      this.socketManager.remove(info.getId());
      UID id = this.socketManager.registerClient(socketIOClient, null);
      info.send("uuid", id);
  }
}
