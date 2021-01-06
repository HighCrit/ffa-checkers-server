package com.highcrit.ffacheckers.socket.lobby;

import static org.mockito.Mockito.mock;

import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.ISocketManager;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LobbyManagerTest {
  private final ISocketManager socketManager = mock(SocketManager.class);
  private LobbyManager lobbyManager;

  @BeforeEach
  private void beforeEach() {
    lobbyManager = new LobbyManager(socketManager);
  }

  @Test
  void create() {
    lobbyManager.create();
    Assertions.assertEquals(1, lobbyManager.getLobbies().size());
  }

  @Test
  void delete() {
    Lobby lobby = lobbyManager.create();
    lobbyManager.delete(lobby.getCode(), "TEST");
    Assertions.assertEquals(0, lobbyManager.getLobbies().size());
  }

  @Test
  void has() {
    Lobby lobby = lobbyManager.create();
    Assertions.assertTrue(lobbyManager.has(lobby.getCode()));
  }

  @Test
  void get() {
    Lobby lobby = lobbyManager.create();
    Assertions.assertEquals(lobby, lobbyManager.get(lobby.getCode()));
  }
}
