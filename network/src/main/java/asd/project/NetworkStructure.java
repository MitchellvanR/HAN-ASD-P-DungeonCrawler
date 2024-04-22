package asd.project;

import static asd.project.domain.event.EventType.NETWORK_SEND_MESSAGE_ISSUE;

import asd.project.domain.dto.serializable.DisconnectClientDTO;
import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.PauseGameDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.exceptions.UnknownEventException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for setting up a network connection. Clients should implement this class to
 * set up a connection to the server, and servers should implement this class to establish a base
 * connection to which clients can connect. <br/> <br/> This class provides a framework for handling
 * input and output through an abstract network structure. Subclasses have to implement the
 * behaviors for starting the connection, handling input, handling output, and handling the
 * publication of objects to subscribers.
 */
public abstract class NetworkStructure {

  protected final Network network;
  private static final Logger LOGGER = Logger.getLogger(NetworkStructure.class.getName());

  protected NetworkStructure(Network network) {
    this.network = network;
  }

  /**
   * Starts the network connection. Subclasses must implement this method to set up the initial
   * connection.
   */
  public abstract void start();

  /**
   * Restarts the network connection. Client must implement this method in order to reconnect
   * to a subhost.
   */
  public abstract void start(int value);

  /**
   * Stops the network connection. This method is used for the player to leave the game and leave
   * the network with it.
   */
  public abstract void leave();

  protected void handleOutput(Object object, ObjectOutputStream objectOutputStream) {
    try {
      if (objectOutputStream != null) {
        objectOutputStream.writeObject(object);
      }
    } catch (IOException e) {
      String exceptionMessage = String.format(
          "Exception thrown in NetworkStructure.handleOutput: %s", e.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
      network.fireEvent(NETWORK_SEND_MESSAGE_ISSUE, "");
    }
  }

  /**
   * Handles the input from the given Socket. Subclasses must implement this method to define how
   * input from the network is handled.
   *
   * @param socket The Socket from which input is received.
   */
  protected abstract void handleInput(Socket socket);

  /**
   * Handles the output of the specified object. Subclasses must implement this method to define how
   * output to the network is handled.
   *
   * @param object The object to be output.
   */
  public abstract void handleOutput(Object object);

  public Event createEvent(Object object) {
    Event event;

    if (object instanceof GameStateDTO) {
      event = new Event(EventType.NETWORK_GAME_STATE, object);
    } else if (object instanceof MessageDTO) {
      event = new Event(EventType.NETWORK_MESSAGE, object);
    } else if (object instanceof StartGameDTO) {
      event = new Event(EventType.NETWORK_GAME_START, object);
    } else if (object instanceof EntityConfig) {
      event = new Event(EventType.NETWORK_SET_ENTITY_CONFIG, object);
    } else if (object instanceof UUID) {
      event = new Event(EventType.NETWORK_REPLACE_PLAYER, object);
    } else if (object instanceof PauseGameDTO) {
      event = new Event(EventType.NETWORK_GAME_PAUSE, object);
      close();
    } else if (object instanceof DisconnectClientDTO) {
      event = new Event(EventType.NETWORK_DISCONNECT_CLIENT, object);
      close();
    } else if (object instanceof SubHostListDTO) {
      event = new Event(EventType.NETWORK_CONNECTION_CHANGE, object);
    } else if (object instanceof SubHostDTO) {
      event = new Event(EventType.NETWORK_CONNECTION_DISCONNECT, object);
    } else {
      throw new UnknownEventException();
    }

    return event;
  }

  public SubHostListDTO getSubHostListFromStorageAndSetInNetwork() {
    network.fireEvent(EventType.NETWORK_SET_SUBHOST_LIST, "");
    return network.getSubHostList();
  }

  public abstract void handleGameStart(StartGameDTO startGameDTO);
  /**
   * Function for updating the client configuration files.
   *
   * @param entityConfig is the updated entity configuration file.
   */
  public abstract void handleSetEntityConfig(EntityConfig entityConfig);

  /**
   * Closes the network connection. Subclasses must implement this method to define how the network
   * connection is closed.
   */
  public abstract void close();

  /**
   * Handles the pause game event.
   */
  public abstract void handlePauseGame();
}
