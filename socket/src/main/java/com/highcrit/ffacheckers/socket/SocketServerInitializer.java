package com.highcrit.ffacheckers.socket;

import com.highcrit.ffacheckers.socket.lobby.LobbyManager;
import com.highcrit.ffacheckers.socket.server.SocketManager;

public class SocketServerInitializer {
  public static void main(String[] args) {
    SocketManager socketManager = new SocketManager();
    socketManager.initialize();
    LobbyManager lobbyManager = new LobbyManager(socketManager);
    lobbyManager.initialize();
  }
}
