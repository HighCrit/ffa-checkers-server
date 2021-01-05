package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinAction;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinResult;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnLobbyJoinTest {
  private SocketManager socketManager;
  private OnLobbyJoin listener;
  private SocketIOClient socket;
  private PlayerClient client;

  @BeforeEach
  void setup() {
    socketManager = new SocketManager();
    listener = new OnLobbyJoin(socketManager.getLobbyManager(), socketManager);
    socket = mock(SocketIOClient.class);
    socketManager.registerClient(socket, null);
    client = socketManager.getInfoByClient(socket);
  }

  @Test
  void onData() {
    Lobby lobby = socketManager.getLobbyManager().create();
    listener.onData(socket, new LobbyJoinAction(lobby.getCode()), null);
    verify(socket)
        .sendEvent(
            LobbyEvent.JOIN_RESULT.getEventName(),
            new LobbyJoinResult(client.getLobby().getCode()));
  }

  @Test
  void onDataWithWrongCode() {
    listener.onData(socket, new LobbyJoinAction(), null);
    verify(socket)
        .sendEvent(
            LobbyEvent.JOIN_RESULT.getEventName(),
            new ActionFailed("Invalid code - no lobby exists with code"));
  }

  @Test
  void onDataWithRemovedClient() {
    socketManager.remove(client.getId());
    listener.onData(socket, null, null);
    verify(socket).sendEvent("send-uuid");
  }
}
