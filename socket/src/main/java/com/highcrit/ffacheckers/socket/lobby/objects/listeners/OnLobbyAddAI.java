package com.highcrit.ffacheckers.socket.lobby.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.socket.lobby.LobbyEvent;
import com.highcrit.ffacheckers.socket.lobby.objects.data.LobbyAddAIResult;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.AIClient;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnLobbyAddAI implements DataListener<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnLobbyAddAI.class);
    private final SocketManager socketManager;

    public OnLobbyAddAI(SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    @Override
    public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) throws Exception {
        PlayerClient info = socketManager.getInfoByClient(socketIOClient);
        if (info == null) {
            LOGGER.error("Tried to handle event of socket that has not been registered");
            socketIOClient.sendEvent("send-uuid"); // Attempt registration
            return;
        }

        if (info.getLobby() != null) {
            if (info.isHost()) {
                AIClient ai = new AIClient();
                info.getLobby().addPlayer(ai.getId(), ai);
                info.send(LobbyEvent.ADD_AI_RESULT, new LobbyAddAIResult(ai.getPlayerColor()));
            } else {
                info.send(LobbyEvent.ADD_AI_RESULT, new ActionFailed("Only the host can add AIs"));
            }
        } else {
            info.send(LobbyEvent.ADD_AI_RESULT, new ActionFailed("You're not in a lobby"));
        }
    }
}
