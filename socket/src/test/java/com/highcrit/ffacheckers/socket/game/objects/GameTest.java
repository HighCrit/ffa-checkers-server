package com.highcrit.ffacheckers.socket.game.objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.domain.entities.Piece;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.socket.game.enums.GameEvent;
import com.highcrit.ffacheckers.socket.game.objects.data.MoveResult;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.lobby.objects.ILobby;
import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.server.objects.clients.AIClient;
import com.highcrit.ffacheckers.socket.server.objects.clients.AbstractClient;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
  private static final String DEFAULT_FEN =
      "Y128,129,130,131,132,137,138,139,140,141,146,147,148,149,150,155,156,157,"
          + "158,159:B36,37,45,46,54,55,63,64,72,73,81,82,90,91,99,100,108,109,117,118:G2,3,4,5,6,11,12,13,14,15,"
          + "20,21,22,23,24,29,30,31,32,33:R43,44,52,53,61,62,70,71,79,80,88,89,97,98,106,107,115,116,124,125";

  private ILobby lobby;
  private Game game;

  @BeforeEach
  private void beforeEach() {
    lobby = new Lobby(mock(LobbyManager.class));
    game = new Game(mock(Lobby.class));
  }

  @Test
  void addPlayer() {
    AbstractClient yellow = new AIClient();

    game.addPlayer(yellow);
    Assertions.assertEquals(1, game.getPlayers().size());
  }

  @Test
  void startNextTurn() {
    PlayerClient client = spy(new PlayerClient(UUID.randomUUID(), mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    for (int i = 0; i < 3; i++) {
      lobby.addPlayer(new AIClient());
    }
    // Last log should be "game-move-set"
    verify(client).send(any(), anyList());
  }

  @Test
  void onMove() {
    UUID id = UUID.randomUUID();
    PlayerClient client = spy(new PlayerClient(id, mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    lobby.addPlayer(new AIClient());
    // Start with 2 players so we can control the board structure
    lobby.getGame().start(DEFAULT_FEN);
    Move firstMove = new Move(131, 121, false);
    lobby.getGame().onMove(client, firstMove);

    verify(client).send(GameEvent.MOVE_RESULT, new MoveResult(firstMove));
  }

  @Test
  void onMoveInvalid() {
    UUID id = UUID.randomUUID();
    PlayerClient client = spy(new PlayerClient(id, mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    lobby.addPlayer(new AIClient());

    lobby.getGame().start(DEFAULT_FEN);
    lobby.getGame().onMove(client, new Move(1, 2, false));

    verify(client).send(GameEvent.MOVE_RESULT, new ActionFailed("Invalid move"));
  }

  @Test
  void onMoveNotCurrentPlayer() {
    lobby.addPlayer(mock(PlayerClient.class));

    UUID id = UUID.randomUUID();
    PlayerClient client = spy(new PlayerClient(id, mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    lobby.getGame().start(DEFAULT_FEN);
    lobby.getGame().onMove(client, new Move());

    verify(client).send(GameEvent.MOVE_RESULT, new ActionFailed("It's not your turn"));
  }

  @Test
  void onMoveCapturingMove() {
    UUID id = UUID.randomUUID();
    PlayerClient client = spy(new PlayerClient(id, mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    lobby.addPlayer(new AIClient());
    // Start with 2 players so we can control the board structure
    lobby.getGame().start("Y155:B147");
    Move firstMove = new Move(155, 138, false, new Piece(PlayerColor.BLUE, 147));
    lobby.getGame().onMove(client, firstMove);

    verify(client).send(GameEvent.MOVE_RESULT, new MoveResult(firstMove));
  }

  @Test
  void onMoveCapturingMoveSequence() {
    UUID id = UUID.randomUUID();
    PlayerClient client = spy(new PlayerClient(id, mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    lobby.addPlayer(new AIClient());
    // Start with 2 players so we can control the board structure
    lobby.getGame().start("Y155:B147,130");
    Move firstMove = new Move(155, 138, false, new Piece(PlayerColor.BLUE, 147));
    Move secondMove = new Move(138, 121, false, new Piece(PlayerColor.BLUE, 130));
    lobby.getGame().onMove(client, firstMove);

    verify(client).send(GameEvent.MOVE_RESULT, new MoveResult(firstMove));
  }

  @Test
  void onMoveCapturingMoveInvalid() {
    UUID id = UUID.randomUUID();
    PlayerClient client = spy(new PlayerClient(id, mock(SocketIOClient.class)));
    lobby.addPlayer(client);
    lobby.onPlayerLoaded(client);

    lobby.addPlayer(new AIClient());
    // Start with 2 players so we can control the board structure
    lobby.getGame().start("Y155:B147");
    Move firstMove = new Move(155, 146, false);
    lobby.getGame().onMove(client, firstMove);

    verify(client).send(GameEvent.MOVE_RESULT, new ActionFailed("Invalid move"));
  }

  @Test
  void removePlayer() {
    AbstractClient client = new AIClient();
    game.addPlayer(client);
    game.removePlayer(client);
    Assertions.assertEquals(0, game.getPlayers().size());
  }

  @Test
  void onPlayerLoaded() {
    AIClient client = mock(AIClient.class, CALLS_REAL_METHODS);
    game.addPlayer(client);
    game.onPlayerLoaded(client);
    verify(client).send(GameEvent.YOUR_COLOR, PlayerColor.YELLOW);
  }
}
