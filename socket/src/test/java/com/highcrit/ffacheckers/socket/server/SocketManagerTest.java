package com.highcrit.ffacheckers.socket.server;

import static org.mockito.Mockito.mock;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocketManagerTest {
  private SocketManager socketManager;

  @BeforeEach
  private void beforeEach() {
    socketManager = new SocketManager();
  }

  @Test
  void has() {
    SocketIOClient ioClient = mock(SocketIOClient.class);
    UID uid = socketManager.registerClient(ioClient, null);
    Assertions.assertTrue(socketManager.has(uid.getId()));
  }

  @Test
  void remove() {
    SocketIOClient ioClient = mock(SocketIOClient.class);
    UID uid = socketManager.registerClient(ioClient, null);
    socketManager.remove(uid.getId());
    Assertions.assertNull(socketManager.getById(uid.getId()));
  }

  @Test
  void getInfoByClient() {
    SocketIOClient ioClient = mock(SocketIOClient.class);
    UID uid = socketManager.registerClient(ioClient, null);
    Assertions.assertEquals(uid.getId(), socketManager.getInfoByClient(ioClient).getId());
  }

  @Test
  void getById() {
    SocketIOClient ioClient = mock(SocketIOClient.class);
    UID uid = socketManager.registerClient(ioClient, null);
    Assertions.assertEquals(uid.getId(), socketManager.getById(uid.getId()).getId());
  }

  @Test
  void registerClient() {
    SocketIOClient ioClient = mock(SocketIOClient.class);
    UID uid = socketManager.registerClient(ioClient, null);
    Assertions.assertTrue(socketManager.getSockets().containsKey(uid.getId()));
  }
}
