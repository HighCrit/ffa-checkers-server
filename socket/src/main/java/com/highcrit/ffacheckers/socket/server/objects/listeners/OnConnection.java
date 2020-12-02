package com.highcrit.ffacheckers.socket.server.objects.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;

public class OnConnection implements ConnectListener {
    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        socketIOClient.sendEvent("send-uuid");
    }
}
