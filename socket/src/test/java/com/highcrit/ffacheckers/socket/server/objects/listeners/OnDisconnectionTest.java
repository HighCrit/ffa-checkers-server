package com.highcrit.ffacheckers.socket.server.objects.listeners;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.concurrent.TimeUnit;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.socket.lobby.objects.ILobby;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnDisconnectionTest {
  private final SocketIOClient socket = mock(SocketIOClient.class);
  private SocketManager socketManager;
  private OnDisconnection listener;

  @BeforeEach
  private void beforeEach() {
    socketManager = new SocketManager();
    listener = new OnDisconnection(socketManager, 0);
  }

  @Test
  void onDisconnect() {
    socketManager.registerClient(socket, null);
    PlayerClient client = socketManager.getInfoByClient(socket);
    ILobby lobby = mock(ILobby.class);
    client.setLobby(lobby);
    listener.onDisconnect(socket);
    verify(lobby).onPlayerDisconnect(client);
  }

  @Test
  void onDisconnectNotInLobby() {
    socketManager.registerClient(socket, null);
    listener.onDisconnect(socket);
    await()
        .atMost(500, TimeUnit.MILLISECONDS)
        .until(() -> socketManager.getInfoByClient(socket) == null);
  }

  @Test
  void onDisconnectHandshakeNotCompleted() {
    listener.onDisconnect(socket);
    verifyNoInteractions(socket);
  }
}
