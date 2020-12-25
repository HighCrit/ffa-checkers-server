package com.highcrit.ffacheckers.socket.server.enums;

import com.highcrit.ffacheckers.domain.communication.objects.Event;

public enum SocketEvents implements Event {
    RESET_UUID("reset-uuid"),
    UUID("uuid");

    private final String eventName;

    SocketEvents(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String getEventName() {
        return eventName;
    }
}
