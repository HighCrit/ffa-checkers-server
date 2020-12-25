package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.lobby.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyClosing;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnLobbyLoaded implements DataListener<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(OnLobbyLoaded.class);
  private final SocketManager socketManager;

  public OnLobbyLoaded(SocketManager socketManager) {
    this.socketManager = socketManager;
  }

  @Override
  public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest)
      throws Exception {
    PlayerClient info = this.socketManager.getInfoByClient(socketIOClient);
    if (info == null) {
      LOGGER.error("Tried to handle event of socket that has not been registered");
      socketIOClient.sendEvent("send-uuid");
      return;
    }

    if (info.getLobby() != null) {
      info.getLobby().onPlayerLoaded(info);
    } else {
      info.send(LobbyEvent.CLOSING, new LobbyClosing("Lobby no longer exists"));
    }
  }
}
