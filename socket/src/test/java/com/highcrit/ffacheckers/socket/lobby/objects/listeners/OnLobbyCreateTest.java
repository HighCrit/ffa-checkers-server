package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinResult;
import com.highcrit.ffacheckers.socket.server.ISocketManager;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnLobbyCreateTest {
  private ISocketManager socketManager;
  private OnLobbyCreate listener;
  private SocketIOClient socket;
  private PlayerClient client;

  @BeforeEach
  void setup() {
    socketManager = new SocketManager();
    listener = new OnLobbyCreate(socketManager.getLobbyManager(), socketManager);
    socket = mock(SocketIOClient.class);
    socketManager.registerClient(socket, null);
    client = socketManager.getInfoByClient(socket);
  }

  @Test
  void onData() {
    listener.onData(socket, null, null);
    verify(socket)
        .sendEvent(
            LobbyEvent.CREATE_RESULT.getEventName(),
            new LobbyJoinResult(client.getLobby().getCode()));
  }

  @Test
  void onDataWithRemovedClient() {
    socketManager.remove(client.getId());
    listener.onData(socket, null, null);
    verify(socket).sendEvent("send-uuid");
  }
}
