package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.GameEvent;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyClosing;
import com.highcrit.ffacheckers.socket.server.ISocketManager;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnLobbyLoadedTest {
  private ISocketManager socketManager;
  private LobbyManager lobbyManager;
  private OnLobbyLoaded listener;
  private SocketIOClient socket;
  private PlayerClient client;

  @BeforeEach
  void setup() {
    socketManager = new SocketManager();
    lobbyManager = new LobbyManager(socketManager);
    listener = new OnLobbyLoaded(socketManager);
    socket = mock(SocketIOClient.class);
    socketManager.registerClient(socket, null);
    client = socketManager.getInfoByClient(socket);
  }

  @Test
  void onData() {
    Lobby lobby = lobbyManager.create();
    lobby.addPlayer(client);

    listener.onData(socket, null, null);
    verify(socket).sendEvent(GameEvent.YOUR_COLOR.getEventName(), PlayerColor.YELLOW);
  }

  @Test
  void onDataNotInLobby() {
    listener.onData(socket, null, null);
    verify(socket)
        .sendEvent(LobbyEvent.CLOSING.getEventName(), new LobbyClosing("Lobby no longer exists"));
  }

  @Test
  void onDataWithRemovedClient() {
    socketManager.remove(client.getId());
    listener.onData(socket, null, null);
    verify(socket).sendEvent("send-uuid");
  }
}
