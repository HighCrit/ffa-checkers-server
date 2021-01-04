package com.highcrit.ffacheckers.socket.lobby.objects.data;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyJoinAction {
  private UUID code;
}
