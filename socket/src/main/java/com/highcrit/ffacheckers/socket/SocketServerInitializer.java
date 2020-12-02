package com.highcrit.ffacheckers.socket;

import com.highcrit.ffacheckers.socket.server.instances.SocketManager;

public class SocketServerInitializer {
  public static void main(String[] args) {
    SocketManager socketManager = new SocketManager();
    socketManager.initialize();
  }
}
