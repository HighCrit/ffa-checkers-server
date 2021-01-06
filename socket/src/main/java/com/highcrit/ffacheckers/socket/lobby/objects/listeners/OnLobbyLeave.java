package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.server.ISocketManager;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class OnLobbyLeave implements DataListener<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(OnLobbyLeave.class);

  private final ISocketManager socketManager;

  @Override
  public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) {
    PlayerClient info = socketManager.getInfoByClient(socketIOClient);
    if (info == null) {
      LOGGER.error("Tried to handle event of socket that has not been registered");
      socketIOClient.sendEvent("send-uuid");
      return;
    }

    if (info.getLobby() != null) {
      info.getLobby().removePlayer(info);
      info.setLobby(null);
    }
  }
}
