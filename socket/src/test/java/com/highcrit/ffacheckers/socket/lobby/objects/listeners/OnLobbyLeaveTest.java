package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyJoinAction;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnLobbyLeaveTest {

  private SocketManager socketManager;
  private OnLobbyLeave listener;
  private SocketIOClient socket;
  private PlayerClient client;

  @BeforeEach
  void setup() {
    socketManager = new SocketManager();
    listener = new OnLobbyLeave(socketManager);
    socket = mock(SocketIOClient.class);
    socketManager.registerClient(socket, null);
    client = socketManager.getInfoByClient(socket);
  }

  @Test
  void onData() {
    Lobby lobby = socketManager.getLobbyManager().create();
    lobby.addPlayer(client);
    listener.onData(socket, null, null);
    Assertions.assertNull(client.getLobby());
  }

  @Test
  void onDataWithRemovedClient() {
    socketManager.remove(client.getId());
    listener.onData(socket, null, null);
    verify(socket).sendEvent("send-uuid");
  }
}
