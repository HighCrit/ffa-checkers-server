package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.domain.communication.objects.Result;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinAction;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnLobbyJoin implements DataListener<LobbyJoinAction> {
  private static final Logger LOGGER = LoggerFactory.getLogger(OnLobbyJoin.class);

  private final LobbyManager lobbyManager;
  private final SocketManager socketManager;

  public OnLobbyJoin(LobbyManager lobbyManager, SocketManager socketManager) {
    this.lobbyManager = lobbyManager;
    this.socketManager = socketManager;
  }

  @Override
  public void onData(
      SocketIOClient socketIOClient, LobbyJoinAction lobbyJoinAction, AckRequest ackRequest) {
    PlayerClient info = this.socketManager.getInfoByClient(socketIOClient);
    if (info == null) {
      LOGGER.error("Tried to handle event of socket that has not been registered");
      socketIOClient.sendEvent("send-uuid"); // Attempt registration
      return;
    }

    Result result;
    if (lobbyManager.has(lobbyJoinAction.getCode())) {
      Lobby lobby = lobbyManager.get(lobbyJoinAction.getCode());
      result = lobby.addPlayer(info);
    } else {
      result = new ActionFailed("Invalid code - no lobby exists with code");
    }

    info.send(LobbyEvent.JOIN_RESULT, result);
  }
}
