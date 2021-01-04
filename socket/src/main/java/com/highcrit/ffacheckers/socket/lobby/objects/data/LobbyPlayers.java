package com.highcrit.ffacheckers.socket.lobby.objects.data;

import java.util.Map;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyPlayers {
  private Map<PlayerColor, String> players;
}
