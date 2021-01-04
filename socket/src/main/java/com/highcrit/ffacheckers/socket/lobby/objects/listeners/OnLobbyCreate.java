package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnLobbyCreate implements DataListener<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(OnLobbyCreate.class);

  private final LobbyManager lobbyManager;
  private final SocketManager socketManager;

  public OnLobbyCreate(LobbyManager lobbyManager, SocketManager socketManager) {
    this.lobbyManager = lobbyManager;
    this.socketManager = socketManager;
  }

  @Override
  public void onData(SocketIOClient socketIOClient, Object object, AckRequest ackRequest) {
    PlayerClient info = socketManager.getInfoByClient(socketIOClient);
    if (info == null) {
      LOGGER.error("Tried to handle event of socket that has not been registered");
      socketIOClient.sendEvent("send-uuid"); // Attempt registration
      return;
    }

    Lobby lobby = this.lobbyManager.create();
    Result result = lobby.addPlayer(info.getId(), info);
    info.setHost(true);
    info.send(LobbyEvent.CREATE_RESULT, result);
  }
}
