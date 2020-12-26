package com.highcrit.ffacheckers.socket.server.objects.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.highcrit.ffacheckers.socket.server.SocketManager;
import com.highcrit.ffacheckers.socket.server.enums.ConnectionState;
import com.highcrit.ffacheckers.socket.server.objects.PlayerClient;
import com.highcrit.ffacheckers.socket.utils.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnDisconnection implements DisconnectListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(OnDisconnection.class);
  private static final TaskScheduler scheduler = new TaskScheduler();
  private static final int DISCONNECTION_TIMEOUT = 30; // 30 seconds
  private final SocketManager socketManager;

  public OnDisconnection(SocketManager socketManager) {
    this.socketManager = socketManager;
  }

  @Override
  public void onDisconnect(SocketIOClient socketIOClient) {
    final PlayerClient info = socketManager.getInfoByClient(socketIOClient);
    if (info == null) {
      return;
    }

    LOGGER.info(String.format("Lost connection to socket: %s", info.getId()));
    // User disconnect set current status to reconnecting
    info.setState(ConnectionState.RECONNECTING);

    if (info.getLobby() != null) {
      // If in a game execute logic for disconnection
      info.getLobby().onPlayerDisconnect(info);
      scheduler.scheduleTask(
          () -> {
            if (info.getState() == ConnectionState.RECONNECTING) {
              // This user is still reconnecting so let's kick them out
              info.getLobby().removePlayer(info);
              info.setLobby(null);
              socketManager.remove(info.getId());
            }
          },
          DISCONNECTION_TIMEOUT);
    } else {
      // If not in a game remove info from memory
      socketManager.remove(info.getId());
    }
  }
}
