package asd.project;

import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.exceptions.ClientGameStartException;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.exceptions.CreateRoomException;
import io.reactivex.rxjava3.core.Observable;
import java.net.ConnectException;

/**
 * The interface Network. This is where all the communications between the peers happen.
 */
public interface INetwork {

  /**
   * Returns the observable for network events.
   *
   * @return The observable for network events.
   */
  Observable<Event> getObservable();

  /**
   * Join a room with the use of a room code.
   *
   * @param roomCode The code for the room that you want to join.
   * @throws ConnectException
   * @throws InvalidRoomCodeException
   */
  boolean joinGame(String roomCode) throws ConnectException, InvalidRoomCodeException;

  /**
   * Create a new room with a random room code.
   *
   * @return the random generated room code.
   */
  String createRoom() throws CreateRoomException;

  /**
   * When people joined the room, this method can be used to start the game.
   *
   * @param startGameDTO Settings that are needed to start the game.
   * @throws ClientGameStartException
   */
  void startGame(StartGameDTO startGameDTO) throws ClientGameStartException;

  /**
   * This method will let the player leave a game without exiting the page. After leaving the game,
   * an agent will be able to replace the leaving player
   */
  void leaveGame();

  /**
   * Function for sending a pause game event to the server and client.
   */
  void pauseGame();

  /**
   * Send the game state to all the other peers.
   *
   * @param gameStateDTO the game state that will be sent.
   */
  void sendGameState(GameStateDTO gameStateDTO);

  /**
   * Send a chat message to all the other peers.
   *
   * @param messageDTO the messages that will be sent.
   */
  void sendMessage(MessageDTO messageDTO);

  /**
   * Set a list of subhosts in storage.
   *
   * @param subHostDTO the list that wil be set.
   */
  void setSubHostList(SubHostListDTO subHostDTO);

  /**
   * Opens a server.
   */
  String openServer() throws CreateRoomException;
  /**
   * Gets a roomcode.
   */
  String getRoomCode();

  /**
   * Sets the roomcode.
   *
   * @param roomCode the string that wil be set.
   */
  void setRoomCode(String roomCode);

  /**
   * fires event to Game through observables.
   *
   * @param eventType is the event for the observables.
   * @param object is the object to be sent through the observable.
   */
  void fireEvent(EventType eventType, Object object);

  /**
   * Function for updating the client configuration files.
   *
   * @param entityConfig is the updated entity configuration file.
   */
  void setEntityConfig(EntityConfig entityConfig);
}
