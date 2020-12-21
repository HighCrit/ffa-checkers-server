package com.highcrit.ffacheckers.socket.lobby.objects.data;

import java.util.Map;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;

public class LobbyPlayers {
    private Map<PlayerColor, String> players;

    public LobbyPlayers(Map<PlayerColor, String> players) {
        this.players = players;
    }

    public Map<PlayerColor, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<PlayerColor, String> players) {
        this.players = players;
    }
}
