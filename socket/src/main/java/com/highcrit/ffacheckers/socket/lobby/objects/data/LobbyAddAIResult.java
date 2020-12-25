package com.highcrit.ffacheckers.socket.lobby.objects.data;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.domain.communication.objects.Result;

public class LobbyAddAIResult implements Result {
    private PlayerColor playerColor;

    public LobbyAddAIResult(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}
