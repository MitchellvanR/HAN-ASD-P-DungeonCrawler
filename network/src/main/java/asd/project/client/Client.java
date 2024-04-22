package asd.project.client;

import static java.awt.Color.RED;

import asd.project.Network;
import asd.project.NetworkStructure;
import asd.project.command.CommandError;
import asd.project.config.world.EntityConfig;
import asd.project.domain.Room;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.dto.KeepAliveDTO;
import asd.project.exceptions.HttpUnauthorizedException;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.server.Server;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends NetworkStructure {

  private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
  private final String ip;
  private final int port;
  private ObjectOutputStream objectOutputStream;
  private Socket socket;
  protected boolean handleHostDisconnectExecuting = false;

  public Client(Network network, String ip, int port) {
    super(network);
    this.ip = ip;
    this.port = port;
  }

  @Override
  public void start() {
    try {
      socket = new Socket();
      socket.connect(new InetSocketAddress(ip, port), 3000);
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

      String connectedMessage = String.format("Connected %s", socket);
      LOGGER.log(Level.INFO, connectedMessage);
      createKeepAliveThread();
      handleInput(socket);
    } catch (IOException exception) {
      String exceptionMessage = String.format("Exception thrown in Client.start: %s",
          exception.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
    }
  }

  private void createKeepAliveThread() {
    new Thread(() -> {
      long timestamp = System.currentTimeMillis();
      while (true) {
        if (System.currentTimeMillis() - timestamp >= 500 && !handleHostDisconnectExecuting) {
          handleOutput(new KeepAliveDTO());
          timestamp = System.currentTimeMillis();
        }
      }
    }).start();
  }

  @Override
  public void start(int value) {
    try {
      socket = new Socket();
      socket.connect(new InetSocketAddress(ip, port), 3000);
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

      LOGGER.log(Level.INFO, () -> String.format("Connected! %s", socket));
      handleInput(socket);
      network.fireEvent(EventType.NETWORK_HOST_FOUND, "");
    } catch (IOException exception) {
      handleHostDisconnectExecuting = false;
      handleHostDisconnect(value + 1);
    }
  }

  @Override
  public void leave() {
    try {
      socket.close();
    } catch (IOException ex) {
      String exceptionMessage = String.format("Exception thrown in Client.leave: %s",
          ex.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
    }
  }

  @Override
  public void handleOutput(Object object) {
    handleOutput(object, objectOutputStream);
  }

  protected void handleHostDisconnect(int value) {
    if (handleHostDisconnectExecuting) {
      return;
    }
    handleHostDisconnectExecuting = true;
    network.fireEvent(EventType.NETWORK_HOST_DISCONNECT, "");
    var subHosts = getSubHostListFromStorageAndSetInNetwork();
    var nextSubHost = subHosts.subHostDTOS().get(value);
    var dropboxService = network.getDropboxService();
    try {
      var hostAddress = InetAddress.getLocalHost().getHostAddress();
      if (nextSubHost.subHost().equals(hostAddress)) {
        network.fireEvent(EventType.NETWORK_CONNECTION_DISCONNECT, nextSubHost);
        Room room = new Room(hostAddress, port);
        dropboxService.updateRoom(network.getRoomCode(), room);
        if (!dropboxService.isCanConnect()) {
          network.fireEvent(EventType.NETWORK_INTERNET_CONNECTION_ISSUES, "");
        } else {
          LOGGER.log(Level.INFO, () -> String.format("New roomcode: %s", network.getRoomCode()));
          network.setNetworkStructure(new Server(network));
          network.startNetworkStructure();
          network.fireEvent(EventType.NETWORK_HOST_FOUND, "");
        }
      } else {
        LOGGER.log(Level.INFO, () -> String.format("New roomcode: %s", network.getRoomCode()));
        socket.close();
        Thread.sleep(6000);
        network.joinGameWhenHostDisconnect(network.getRoomCode(), value);
      }
    } catch (HttpUnauthorizedException | UnknownHostException | InterruptedException exception) {
      LOGGER.log(Level.SEVERE, String.format("Could not find new host adress, %s.", exception));
      Thread.currentThread().interrupt();
    } catch (InvalidRoomCodeException | IOException e) {
      throw new RuntimeException(String.format("Encountered error: %s,", e));
    }
  }

  @Override
  protected void handleInput(Socket socket) {
    try {
      var objectInputStream = new ObjectInputStream(socket.getInputStream());

      new Thread(() -> {
        try {
          while (!socket.isClosed()) {
            var object = objectInputStream.readObject();
            Event event = createEvent(object);
            network.publish(event);
          }
        } catch (SocketException e) {
          LOGGER.log(Level.INFO, "The socket of this client has been closed.");
          handleHostDisconnect(0);
        } catch (IOException | ClassNotFoundException ex) {
          String exceptionMessage = String.format("Exception thrown in Client.handleInput: %s",
              ex.getMessage());
          LOGGER.log(Level.SEVERE, exceptionMessage);
        }
      }).start();
    } catch (IOException ex) {
      String exceptionMessage = String.format("Exception thrown in Client.handleInput: %s",
          ex.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
    }
  }

  @Override
  public void handleGameStart(StartGameDTO startGameDTO) {
    var event = createEvent(new MessageDTO("You cannot start the game!", Color.RED));
    network.publish(event);
  }

  @Override
  public void handleSetEntityConfig(EntityConfig entityConfig) {
    var event = createEvent(new MessageDTO(
        CommandError.ERROR_CLIENT_CONFIG.toMessage().value(), RED));
    network.publish(event);
  }

  @Override
  public void handlePauseGame() {
    var event = createEvent(new MessageDTO("You can't pause the game as a client!"));
    network.publish(event);
  }

  @Override
  public void close() {
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException ex) {
        String exceptionMessage = String.format("Exception thrown in Client.close: %s",
            ex.getMessage());
        LOGGER.log(Level.SEVERE, exceptionMessage);
      }
    }
  }
}
