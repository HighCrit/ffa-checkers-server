package com.highcrit.ffacheckers.socket.server;

import java.util.UUID;

import com.corundumstudio.socketio.SocketIOClient;
import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.server.objects.clients.PlayerClient;
import com.highcrit.ffacheckers.socket.server.objects.data.UID;

public interface ISocketManager {
    LobbyManager getLobbyManager();
    void joinRoom(UUID id, UUID room);
    void clearRoom(UUID code);
    PlayerClient getInfoByClient(SocketIOClient socketIOClient);
    PlayerClient getById(UUID id);
    UID registerClient(SocketIOClient socket, UUID knownId);
    void remove(UUID id);
}
