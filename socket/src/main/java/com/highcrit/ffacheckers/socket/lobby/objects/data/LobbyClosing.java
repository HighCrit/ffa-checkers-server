package com.highcrit.ffacheckers.socket.lobby.objects.data;

public class LobbyClosing {
    private final String reason;

    public LobbyClosing(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
