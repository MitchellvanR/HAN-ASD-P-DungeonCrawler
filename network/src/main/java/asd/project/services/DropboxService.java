package asd.project.services;

import static asd.project.constants.DropboxTokens.APP_KEY;
import static asd.project.constants.DropboxTokens.APP_SECRET;
import static asd.project.constants.DropboxTokens.REFRESH_TOKEN;

import asd.project.domain.Room;
import asd.project.dto.GameCodeFileDto;
import asd.project.exceptions.HttpRequestException;
import asd.project.exceptions.HttpUnauthorizedException;
import asd.project.serializers.ISerializer;
import com.google.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

public class DropboxService implements IDropboxService {

  private static final String ERROR_MESSAGE = "An error occurred: %s";
  private static final String AUTHORIZATION = "Authorization";
  private static final String DROPBOX_API = "Dropbox-API-Arg";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final Logger LOGGER = Logger.getLogger(DropboxService.class.getName());
  private final ISerializer serializer;
  private final HttpClient httpClient;
  private String accessToken;
  private String roomCode;
  private boolean canConnect = true;

  @Inject
  public DropboxService(ISerializer serializer) {
    this.serializer = serializer;
    this.httpClient = HttpClient.newHttpClient();
  }

  public String uploadRoom(Room room, boolean retry) {
    canConnect = true;
    try {
      if (accessToken == null || accessToken.trim().isEmpty()) {
        if (!retry) {
          return null;
        }
        accessToken = getAccessToken();
        retry = false;
      }

      roomCode = generateAndReturnRoomCode();

      uploadRoom(room, roomCode);

      return roomCode;
    } catch (HttpUnauthorizedException ex) {
      if (!retry) {
        String message = String.format(ERROR_MESSAGE, ex);
        LOGGER.log(Level.WARNING, message);
        return null;
      }

      return uploadRoom(room, false);
    }
  }

  public void updateRoom(String roomCode, Room room) throws HttpUnauthorizedException {
    canConnect = true;
    accessToken = getAccessToken();
    deleteFile(roomCode, accessToken);
    uploadRoom(room, roomCode);
  }

  public GameCodeFileDto getGamecodeFileDto(String gameCode, boolean retry) {
    canConnect = true;
    if (accessToken == null || accessToken.trim().isEmpty()) {
      if (!retry) {
        return null;
      }
      accessToken = getAccessToken();
      retry = false;
    }

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://content.dropboxapi.com/2/files/download"))
        .header(AUTHORIZATION, "Bearer " + accessToken).header(CONTENT_TYPE, "text/plain")
        .header(DROPBOX_API, String.format("{\"path\":\"/DC_%s.txt\"}", gameCode)).build();
    try {
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 409) {
        return null;
      }

      return serializer.deserialize(GameCodeFileDto.class, response.body());

    } catch (IOException | InterruptedException | NullPointerException exception) {
      canConnect = false;
      if (!retry) {
        String message = String.format(ERROR_MESSAGE, exception);
        LOGGER.log(Level.WARNING, message);
        Thread.currentThread().interrupt();
        return null;
      }
    }
    return getGamecodeFileDto(gameCode, false);
  }

  protected String getAccessToken() {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(
        "https://api.dropbox.com/oauth2/token?grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s",
        REFRESH_TOKEN, APP_KEY, APP_SECRET))).method("POST", BodyPublishers.noBody()).build();
    try {
      var response = handleResponse(httpClient.send(request, BodyHandlers.ofString()));
      return response != null ? response.split("[, :\"]+")[2] : null;

    } catch (IOException | InterruptedException | HttpRequestException |
             HttpUnauthorizedException ex) {
      canConnect = false;
      String message = String.format(ERROR_MESSAGE, ex);
      LOGGER.log(Level.WARNING, message);
      Thread.currentThread().interrupt();
      return null;
    }
  }

  protected boolean doesGameExist(String roomCode) throws HttpUnauthorizedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.dropboxapi.com/2/files/search_v2"))
        .header(AUTHORIZATION, "Bearer " + accessToken).header(CONTENT_TYPE, "application/json")
        .POST(BodyPublishers.ofString(String.format("{\"query\":\"%s\"}", roomCode))).build();
    try {
      String response = handleResponse(httpClient.send(request, BodyHandlers.ofString()));
      return !(response != null && response.contains("\"matches\":[]"));
    } catch (IOException | InterruptedException | NullPointerException | HttpRequestException ex) {
      canConnect = false;
      String message = String.format(ERROR_MESSAGE, ex);
      LOGGER.log(Level.WARNING, message);
      Thread.currentThread().interrupt();
      return false;
    }
  }

  protected void uploadRoom(Room room, String roomCode) throws HttpUnauthorizedException {
    String fileContent = serializer.serialize(room);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://content.dropboxapi.com/2/files/upload"))
        .header(AUTHORIZATION, String.format("Bearer %s", accessToken)).header(DROPBOX_API,
            String.format(
                "{\"autorename\":false,\"mode\":\"add\",\"mute\":false,\"path\":\"/DC_%s.txt\",\"strict_conflict\":false}",
                roomCode)).header(CONTENT_TYPE, "application/octet-stream")
        .POST(BodyPublishers.ofString(fileContent)).build();
    try {
      handleResponse(httpClient.send(request, BodyHandlers.ofString()));
    } catch (IOException | InterruptedException | NullPointerException | HttpRequestException ex) {
      canConnect = false;
      String message = String.format(ERROR_MESSAGE, ex);
      LOGGER.log(Level.WARNING, message);
      Thread.currentThread().interrupt();
    }
  }

  protected void deleteFile(String gamecode, String accessToken) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.dropboxapi.com/2/files/delete_v2"))
        .header("Authorization", "Bearer " + accessToken)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString("{\"path\":\"/DC_" + gamecode + ".txt\"}"))
        .build();
    try {
      HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException exception) {
      canConnect = false;
      LOGGER.log(Level.WARNING, String.format("An error occurred: %s.", exception));
      Thread.currentThread().interrupt();
    }
  }

  protected <T> T handleResponse(HttpResponse<T> response)
      throws HttpRequestException, HttpUnauthorizedException {
    if (response == null) {
      return null;
    }

    String errorMessage;
    return switch (response.statusCode()) {
      case HttpURLConnection.HTTP_OK -> response.body();
      case HttpURLConnection.HTTP_UNAUTHORIZED -> {
        errorMessage = (String) response.body();
        throw new HttpUnauthorizedException(String.format("Message: %s", errorMessage));
      }
      case HttpURLConnection.HTTP_BAD_REQUEST -> {
        errorMessage = (String) response.body();
        throw new HttpRequestException(String.format("Message: %s", errorMessage));
      }
      default -> null;
    };
  }

  protected String generateAndReturnRoomCode() throws HttpUnauthorizedException {
    boolean gameExists = true;
    String generatedRoomcode = null;
    while (gameExists) {
      generatedRoomcode = RandomStringUtils.randomAlphabetic(5).toUpperCase();
      gameExists = doesGameExist(generatedRoomcode);
    }

    return generatedRoomcode;
  }

  public String getRoomCode() {
    return roomCode;
  }

  public void setRoomCode(String roomCode) {
    this.roomCode = roomCode;
  }

  public boolean isCanConnect() {
    return canConnect;
  }
}

