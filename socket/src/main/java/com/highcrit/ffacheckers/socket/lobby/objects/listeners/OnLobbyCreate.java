package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.lobby.instances.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.instances.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.socket.utils.data.Result;
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

    info.setLobby(lobby);
    info.send("lobby-create-result", result);
  }
}
