package com.highcrit.ffacheckers.socket.server.objects;

import com.corundumstudio.socketio.SocketIOClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PlayerClient extends AbstractClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerClient.class);

	private SocketIOClient socket;

	public PlayerClient(UUID id, SocketIOClient socket) {
		super(id);
		this.socket = socket;
	}

	@Override
	public void send(String eventName, Object data) {
		LOGGER.info(String.format("Sending event \"%s\" to socket id: %s", eventName, id));
		socket.sendEvent(eventName, data);
	}

	public SocketIOClient getSocket() {
		return socket;
	}

	public void setSocket(SocketIOClient socket) {
		this.socket = socket;
	}
}
