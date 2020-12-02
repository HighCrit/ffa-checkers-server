package com.highcrit.ffacheckers.socket.lobby.instances;

import java.util.HashMap;

import com.highcrit.ffacheckers.socket.lobby.objects.Lobby;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyClosing;
import com.highcrit.ffacheckers.socket.server.instances.SocketManager;
import com.highcrit.ffacheckers.socket.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobbyManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(LobbyManager.class);

  private final SocketManager socketManager;
  private final HashMap<String, Lobby> lobbies = new HashMap<>();

    public LobbyManager(SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    public String create() {
        int retryLength = 4;
        String code = StringUtil.generateCode(retryLength);

        // Tries to find a unique lobby code
        while (this.lobbies.containsKey(code)) {
            code = StringUtil.generateCode(retryLength);
            // if no unique code was generated after two attempts increase code length by one
            retryLength++;
        }

        final Lobby lobby = new Lobby(this, code);
        this.lobbies.put(code, lobby);

        return code;
    }

    public void delete(String code, String reason) {
        Lobby lobby = lobbies.get(code);
        if (lobby == null) {
            return;
        }
        LOGGER.info(String.format("Closing lobby {%s) with reason: %s", code, reason));
        lobby.send("lobby-closing", new LobbyClosing(reason));
        lobby.delete();
        socketManager.clearRoom(code);
        lobbies.remove(code);
    }
}
