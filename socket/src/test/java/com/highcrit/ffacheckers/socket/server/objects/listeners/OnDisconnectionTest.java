package com.highcrit.ffacheckers.socket.server.objects.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.socket.lobby.objects.ILobby;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnDisconnectionTest {
  private SocketIOClient socket = mock(SocketIOClient.class);
  private SocketManager socketManager;
  private OnDisconnection listener;

  @BeforeEach
  private void beforeEach() {
    socketManager = new SocketManager();
    listener = new OnDisconnection(socketManager, 0);
  }

  @Test
  void onDisconnect() throws InterruptedException {
    socketManager.registerClient(socket, null);
    PlayerClient client = socketManager.getInfoByClient(socket);
    ILobby lobby = mock(ILobby.class);
    client.setLobby(lobby);
    listener.onDisconnect(socket);
    Thread.sleep(500); // Wait a second
    verify(lobby).onPlayerDisconnect(client);
  }

  @Test
  void onDisconnectNotInLobby() throws InterruptedException {
    socketManager.registerClient(socket, null);
    listener.onDisconnect(socket);
    Thread.sleep(500); // Wait a second
    Assertions.assertNull(socketManager.getInfoByClient(socket));
  }

  @Test
  void onDisconnectHandshakeNotCompleted() {
    listener.onDisconnect(socket);
    verifyNoInteractions(socket);
  }
}
