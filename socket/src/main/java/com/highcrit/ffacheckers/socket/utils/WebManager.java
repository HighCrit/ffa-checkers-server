package com.highcrit.ffacheckers.socket.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highcrit.ffacheckers.domain.entities.Replay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebManager.class);
  private static final String REPLAY_URL = "http://api:8080/api/replays/";

  private WebManager() {
    throw new IllegalStateException("Utility class");
  }

  public static void saveReplay(Replay replay) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String requestBody = objectMapper.writeValueAsString(replay);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(REPLAY_URL))
              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
              .build();

      client
          .sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(
              response -> {
                LOGGER.info(
                    String.format(
                        "Saving replay with id: %s returned status code: %d",
                        replay.getId(), response.statusCode()));
                return response;
              });
    } catch (JsonProcessingException ex) {
      LOGGER.error(String.format("Failed to save replay with id: %s", replay.getId()));
      ex.printStackTrace();
    }
  }

  public static void deleteReplay(UUID id) {
    LOGGER.info(String.format("Deleting replays with id: %s", id));
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create(REPLAY_URL + id)).DELETE().build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
  }
}
