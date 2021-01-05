package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.enums.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyAddAIResult;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnLobbyAddAITest {
  private SocketManager socketManager;
  private OnLobbyAddAI listener;
  private SocketIOClient socket;
  private PlayerClient client;
  private Lobby lobby;

  @BeforeEach
  void setup() {
    socketManager = new SocketManager();
    listener = new OnLobbyAddAI(socketManager);
    socket = mock(SocketIOClient.class);
    socketManager.registerClient(socket, null);
    lobby = new Lobby(mock(LobbyManager.class));
    client = socketManager.getInfoByClient(socket);
  }

  @Test
  void onData() {
    client.setLobby(lobby);
    client.setHost(true);
    listener.onData(socket, null, null);
    verify(socket)
        .sendEvent(
            LobbyEvent.ADD_AI_RESULT.getEventName(), new LobbyAddAIResult(PlayerColor.YELLOW));
  }

  @Test
  void onDataWhenNotHost() {
    client.setLobby(lobby);
    client.setHost(false);
    listener.onData(socket, null, null);
    verify(socket)
        .sendEvent(
            LobbyEvent.ADD_AI_RESULT.getEventName(), new ActionFailed("Only the host can add AIs"));
  }

  @Test
  void onDataWhenNotInLobby() {
    listener.onData(socket, null, null);
    verify(socket)
        .sendEvent(
            LobbyEvent.ADD_AI_RESULT.getEventName(), new ActionFailed("You're not in a lobby"));
  }

  @Test
  void onDataWithRemovedClient() {
    socketManager.remove(client.getId());
    listener.onData(socket, null, null);
    verify(socket).sendEvent("send-uuid");
  }
}
