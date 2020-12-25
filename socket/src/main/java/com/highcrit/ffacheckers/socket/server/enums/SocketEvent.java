package com.highcrit.ffacheckers.socket.server.enums;

import com.highcrit.ffacheckers.domain.communication.objects.Event;

public enum SocketEvent implements Event {
    /* Inbound */
    RESET_UUID("reset-uuid"),
    UUID("uuid"),

    /* Outbound */
    SEND_UUID("send-uuid");

    private final String eventName;

    SocketEvent(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String getEventName() {
        return eventName;
    }
}
