package asd.project.services;

import asd.project.domain.Room;
import asd.project.dto.GameCodeFileDto;
import asd.project.exceptions.HttpUnauthorizedException;

/**
 * Interface for the DropboxService, created to be used with dependency injection
 */
public interface IDropboxService {

  /**
   * Uploads the room and returns a room code for other people to join the room
   *
   * @param room The room to upload
   * @param retry When true, it uses a retry strategy to reauthorize for the api when it returns 401 unauthorized
   *
   * @return The room code to join the room
   */
  String uploadRoom(Room room, boolean retry);

  /**
   * Gets the game code file linked to the given game code
   *
   * @param gameCode The game code given by the user
   * @param retry When true, it uses a retry strategy to reauthorize for the api when it returns 401 unauthorized
   *
   * @return The game code file used for joining the game
   */
  GameCodeFileDto getGamecodeFileDto(String gameCode, boolean retry);

  /**
   * Updates the room
   *
   * @param roomCode The room code of the room
   * @param room The room to update
   */
  void updateRoom(String roomCode, Room room) throws HttpUnauthorizedException;

  /**
   * Gets the room code
   *
   *
   * @return The room code
   */
  String getRoomCode();

  /**
   * Sets the room code
   *
   * @param roomCode The room code to set
   */
  void setRoomCode(String roomCode);

  /**
   * Returns if it is possible to connect to the room
   *
   * @return If it is possible to connect to the room
   */
  boolean isCanConnect();
}

