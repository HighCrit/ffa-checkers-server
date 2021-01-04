package com.highcrit.ffacheckers.socket.lobby.objects;

import static org.mockito.Mockito.mock;

import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LobbyTest {
  private final LobbyManager lobbyManager = mock(LobbyManager.class);
  private Lobby lobby;

  @BeforeEach
  private void beforeEach() {
    lobby = new Lobby(lobbyManager);
  }

  @Test
  void addPlayer() {
    PlayerClient client = mock(PlayerClient.class);
    lobby.addPlayer(client);
    Assertions.assertEquals(1, lobby.getConnectedClients().size());
  }

  @Test
  void removePlayer() {
    PlayerClient client = mock(PlayerClient.class);
    lobby.addPlayer(client);
    lobby.removePlayer(client);
    Assertions.assertEquals(0, lobby.getConnectedClients().size());
  }

  @Test
  void delete() {
    PlayerClient client = mock(PlayerClient.class);
    lobby.addPlayer(client);
    lobby.delete();
    Assertions.assertNull(client.getLobby());
  }
}
