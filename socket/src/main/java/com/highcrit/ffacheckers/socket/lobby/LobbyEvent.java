package com.highcrit.ffacheckers.socket.lobby;

import com.highcrit.ffacheckers.domain.communication.objects.Event;

public enum LobbyEvent implements Event {
    /* Inbound */
    CREATE_ACTION("lobby-create-action"),
    JOIN_ACTION("lobby-join-action"),
    LEAVE("lobby-leave-action"),
    ADD_AI_ACTION("lobby-add-ai-action"),
    LOADED("lobby-loaded"),

    /* Outbound */
    CREATE_RESULT("lobby-create-result"),
    JOIN_RESULT("lobby-join-result"),
    ADD_AI_RESULT("lobby-add-ai-result"),
    PLAYER_JOINED("lobby-player-joined"),
    PLAYER_RECONNECT("lobby-reconnect"),
    PLAYER_DISCONNECT("lobby-player-disconnect"),
    CLOSING("lobby-closing");

    private final String eventName;

    LobbyEvent(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String getEventName() {
        return eventName;
    }
}
