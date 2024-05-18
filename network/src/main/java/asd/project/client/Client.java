package asd.project.client;

import static java.awt.Color.RED;

import asd.project.Network;
import asd.project.NetworkStructure;
import asd.project.command.CommandError;
import asd.project.config.world.EntityConfig;
import asd.project.domain.Room;
import asd.project.domain.dto.SubHostDTO;
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
import java.net.*;
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
      createClient();
    } catch (IOException e) {
      handleIOException(e);
    }
  }

  private void createClient() throws IOException {
    var socket = createClientSocket();
    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    LOGGER.log(Level.INFO, String.format("Connected %s", socket));
    createKeepAliveThread();
    handleInput(socket);
  }

  private Socket createClientSocket() throws IOException {
    socket = new Socket();
    socket.connect(new InetSocketAddress(ip, port), 3000);
    return socket;
  }

  private void createKeepAliveThread() {
    new Thread(this::startKeepAlive).start();
  }

  private void startKeepAlive() {
    long timestamp = System.currentTimeMillis();
    while (true) {
      timestamp = evaluateConnectionInterval(timestamp);
    }
  }

  private long evaluateConnectionInterval(long timestamp) {
    if (!checkConnectionInterval(timestamp)) return System.currentTimeMillis();
    handleOutput(new KeepAliveDTO());
    return System.currentTimeMillis();
  }

  private boolean checkConnectionInterval(long timestamp) {
    return System.currentTimeMillis() - timestamp >= 500 && !handleHostDisconnectExecuting;
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
    if (handleHostDisconnectExecuting) return;
    startHandlingHostDisconnect();
    handleHostDisconnectSafely(value);
  }

  protected void startHandlingHostDisconnect() {
    handleHostDisconnectExecuting = true;
    network.fireEvent(EventType.NETWORK_HOST_DISCONNECT, "");
  }

  protected void handleHostDisconnectSafely(int value) {
    try {
      handleNewHostConnections(value);
    } catch (UnknownHostException e) {
      handleUnknownHostException(e);
    } catch (IOException | InterruptedException | InvalidRoomCodeException e) {
      throw new RuntimeException(e);
    }
  }

  protected void handleUnknownHostException(UnknownHostException e) {
    LOGGER.log(Level.SEVERE, String.format("Could not find new host adress, %s.", e));
    Thread.currentThread().interrupt();
  }

  protected void handleNewHostConnections(int value) throws IOException, InvalidRoomCodeException, InterruptedException {
    var subHosts = getSubHostListFromStorageAndSetInNetwork();
    var nextSubHost = subHosts.subHostDTOS().get(value);

    if (isDifferentHost(nextSubHost)) {
      joinNewHost(value);
      return;
    }
    if (!canConnectToDropboxService()) {
      network.fireEvent(EventType.NETWORK_INTERNET_CONNECTION_ISSUES, "");
      return;
    }
    hostNewRoom();
  }

  private boolean isDifferentHost(SubHostDTO nextSubHost) throws UnknownHostException {
    var hostAddress = InetAddress.getLocalHost().getHostAddress();
    return !nextSubHost.subHost().equals(hostAddress);
  }

  private boolean canConnectToDropboxService() {
    var dropboxService = network.getDropboxService();
    return dropboxService.isCanConnect();
  }

  protected void joinNewHost(int value) throws IOException, InterruptedException, InvalidRoomCodeException {
    LOGGER.log(Level.INFO, () -> String.format("New roomcode: %s", network.getRoomCode()));
    socket.close();
    Thread.sleep(6000);
    network.joinGameWhenHostDisconnect(network.getRoomCode(), value);
  }

  protected void hostNewRoom() {
    LOGGER.log(Level.INFO, () -> String.format("New roomcode: %s", network.getRoomCode()));
    network.setNetworkStructure(new Server(network));
    network.startNetworkStructure();
    network.fireEvent(EventType.NETWORK_HOST_FOUND, "");
  }

  @Override
  protected void handleInput(Socket socket) {
    try {
      handleInputRefactor(socket);
    } catch (IOException e) {
      handleIOException(e);
    }
  }

  private void handleInputRefactor(Socket socket) throws IOException {
    var objectInputStream = new ObjectInputStream(socket.getInputStream());
    new Thread(() -> {
      listenForInputSafely(objectInputStream, socket);
    }).start();
  }

  private void handleIOException(IOException e) {
    String exceptionMessage = String.format("Exception thrown in Client.handleInput: %s", e.getMessage());
    LOGGER.log(Level.SEVERE, exceptionMessage);
  }

  private void listenForInputSafely(ObjectInputStream objectInputStream, Socket socket) {
    try {
      listenForInput(objectInputStream, socket);
    } catch (SocketException e) {
      handleSocketException(e);
    } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
  }

  private void handleSocketException(SocketException e) {
    LOGGER.log(Level.INFO, "The socket of this client has been closed.");
    handleHostDisconnect(0);
  }

  private void listenForInput(ObjectInputStream objectInputStream, Socket socket) throws IOException, ClassNotFoundException {
    while (!socket.isClosed()) {
      var object = objectInputStream.readObject();
      Event event = createEvent(object);
      network.publish(event);
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
