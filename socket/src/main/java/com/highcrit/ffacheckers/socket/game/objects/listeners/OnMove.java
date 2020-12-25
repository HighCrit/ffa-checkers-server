package com.highcrit.ffacheckers.socket.game.objects.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnMove implements DataListener<Move> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnMove.class);

    private final SocketManager socketManager;

    public OnMove(SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    @Override
    public void onData(SocketIOClient socketIOClient, Move move, AckRequest ackRequest) throws Exception {
        PlayerClient client = socketManager.getInfoByClient(socketIOClient);
        if (client == null) {
            LOGGER.error("Tried to handle event of socket that has not been registered");
            socketIOClient.sendEvent("send-uuid");
            return;
        }

        if (client.getLobby() != null) {
            client.getLobby().getGame().onMove(client, move);
        }
    }
}
