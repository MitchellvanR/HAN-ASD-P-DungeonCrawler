package asd.project;

import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.domain.dto.storage.StorageGameStateDTO;
import asd.project.domain.dto.storage.StorageWorldConfiguration;
import asd.project.domain.entity.Player;
import java.util.UUID;

/**
 * Interface implementation for storing the player state in a storage. Possible to retrieve the
 * player state from a storage.
 */
public interface IStorage {

  /**
   * Method for saving the current state of the Player.
   *
   * @param playerState The playerState that needs to be saved in the database.
   */
  void savePlayerState(Player playerState);

  /**
   * Method for saving a new SubHost.
   *
   * @param subHostDTO The SubHost that needs to be saved in the database.
   */
  void saveSubHost(SubHostListDTO subHostDTO);

  /**
   * Method for deleting a subHost from the database.
   *
   * @param ip The ip of the subHost that needs to be deleted from the database.
   */
  void deleteSubHost(SubHostDTO subHostDTO);

  /**
   * Method for getting all subHosts in a game room
   *
   * @return The SubHostList that needs to be fetched from the database.
   */
  SubHostListDTO getSubHostList();

  /**
   * Gets current state of given player.
   *
   * @return The current state of the player
   */
  Player getPlayerState(Player player);

  void deleteAllSubhosts();
  /**
   * Gets current state of given player.
   *
   * @return The current state of the player
   */
  Player getPlayerState(UUID uuid);

  /**
   * Method for saving the game to play again another time.
   *
   * @param storageGameStateDTO       The game state that needs to be saved in the pause game file.
   * @param storageWorldConfiguration The world configuration that needs to be saved in the pause
   *                                  game file.
   * @param roomCode                  The room code for the name of the file.
   */
  void sendGameStateToDatabase(StorageGameStateDTO storageGameStateDTO,
      StorageWorldConfiguration storageWorldConfiguration, String roomCode);

  /**
   * Method for deleting a player from the database.
   *
   * @param player The player that needs to be deleted from the database.
   */
  void deletePlayer(Player player);

  /**
   * Method that gets the amount of knights that are still in the database.
   *
   * @return The amount of knights alive.
   */
  int getAmountOfKnightsAlive();

}
