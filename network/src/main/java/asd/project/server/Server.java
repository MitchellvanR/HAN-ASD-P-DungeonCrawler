package asd.project.server;

import asd.project.Network;
import asd.project.NetworkStructure;
import asd.project.config.world.EntityConfig;
import asd.project.constants.NetworkConstants;
import asd.project.domain.ClientSocket;
import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.domain.dto.serializable.DisconnectClientDTO;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.PauseGameDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.event.Event;
import asd.project.dto.KeepAliveDTO;
import asd.project.exceptions.PortAlreadyKnownException;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends NetworkStructure {

  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
  private final List<ObjectOutputStream> objectOutputStreamList;
  private int port = 0;
  private List<ClientSocket> clientSocketList;
  private ServerSocket serverSocket;

  public Server(Network network) {
    super(network);
    objectOutputStreamList = new ArrayList<>();
    clientSocketList = new ArrayList<>();
  }

  @Override
  public void start() {
    try {
      serverSocket = new ServerSocket(0);
      port = serverSocket.getLocalPort();
      LOGGER.log(Level.INFO, () ->
          String.format("Server started on port %s. Waiting for connections...", port));
      while (!serverSocket.isClosed()) {

        Socket socket = serverSocket.accept();

        var clientSocket = new ClientSocket();
        clientSocket.setSocket(socket);
        var objectOutputStream = new ObjectOutputStream(clientSocket.getSocket().getOutputStream());

        String newConnectionMessage = String.format("New connection: %s", clientSocket);
        LOGGER.log(Level.INFO, newConnectionMessage);

        objectOutputStreamList.add(objectOutputStream);
        clientSocketList.add(clientSocket);

        handleInput(socket);

        List<String> subHostList = clientSocketList.stream()
            .map(client -> client.getInetAddress().toString().replace("/", "")).toList();
        var subHostListDTO = new SubHostListDTO(new ArrayList<>());
        for (String ip : subHostList) {
          subHostListDTO.subHostDTOS().add(new SubHostDTO(ip, network.getRoomCode()));
        }
        handleOutput(subHostListDTO);
        getSubHostListFromStorageAndSetInNetwork();
      }
    } catch (SocketException ignored) {
      LOGGER.log(Level.INFO, "Socket closed");
    } catch (IOException ex) {
      String exceptionMessage = String.format("Exception thrown in Server.start: %s",
          ex.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
    }
  }

  @Override
  public void leave() {
    // Not implemented
  }

  @Override
  public void handleOutput(Object object) {
    synchronized (objectOutputStreamList) {
      for (var objectOutputStream : objectOutputStreamList) {
        handleOutput(object, objectOutputStream);
      }
      Event event = createEvent(object);
      network.publish(event);
    }
  }

  private void handleClientDisconnect(Socket socket) {
    handleOutput(new SubHostDTO(socket.getInetAddress().toString().replace("/", ""), ""));
  }

  @Override
  protected void handleInput(Socket socket) {
    try {
      var objectInputStream = new ObjectInputStream(socket.getInputStream());

      new Thread(() -> {
        var clientSocket = clientSocketList.stream()
            .filter(cs -> cs.getSocket() == socket).findFirst().orElse(null);
        try {
          boolean uuidSet = false;
          while (clientSocket != null) {
            var object = objectInputStream.readObject();
            if (object instanceof KeepAliveDTO) {
              continue;
            }
            handleOutput(object);

            if (!uuidSet && object instanceof GameStateDTO gameStateDTO) {
              uuidSet = handleFirstState(gameStateDTO, socket);
            }
          }
        } catch (IOException | ClassNotFoundException e) {
          removeClient(clientSocket);
          handleClientDisconnect(socket);
        }
      }).start();
    } catch (IOException ex) {
      String exceptionMessage = String.format("Exception thrown in Server.handleInput(): %s",
          ex.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
    }
  }

  @Override
  public void handleGameStart(StartGameDTO startGameDTO) {
    if (clientSocketList.isEmpty()) {
      handleOutput(new MessageDTO("There are not enough players to start the game!"));
      return;
    } else if (clientSocketList.size() >= NetworkConstants.MAX_PLAYER_COUNT) {
      handleOutput(new MessageDTO("There are too many players to start the game!"));
      return;
    }

    String subHostListString = String.join(", ",
        clientSocketList.stream().map(
                clientSocket -> clientSocket.getSocket().getInetAddress().toString().replace("/", ""))
            .toList());
    var messageDTO = new MessageDTO("Sub hosts: " + subHostListString, Color.BLUE);
    handleOutput(messageDTO);
    handleOutput(startGameDTO);
  }

  @Override
  public void handleSetEntityConfig(EntityConfig entityConfig) {
    handleOutput(entityConfig);
  }

  @Override
  public void handlePauseGame() {
    var event = createEvent(new PauseGameDTO());
    network.publish(event);
    handleOutput(new DisconnectClientDTO());
  }

  @Override
  public void close() {
    try {
      serverSocket.close();
    } catch (IOException ex) {
      String exceptionMessage = String.format("Exception thrown in Server.close(): %s",
          ex.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
    }
  }

  public List<ClientSocket> getClientSocketList() {
    return clientSocketList;
  }

  public void setClientSocketList(List<ClientSocket> clientSocketList) {
    this.clientSocketList = clientSocketList;
  }

  private void removeClient(ClientSocket clientSocket) {
    objectOutputStreamList.remove(clientSocketList.indexOf(clientSocket));
    clientSocketList.remove(clientSocket);

    if (clientSocket.getClientUuid() != null) {
      var event = createEvent(clientSocket.getClientUuid());
      network.publish(event);
    }

    var messageDTO = new MessageDTO("Client disconnected: " + clientSocket.getSocket());
    handleOutput(messageDTO);
  }

  private boolean handleFirstState(GameStateDTO gameStateDTO, Socket socket) {
    var clientSocket = clientSocketList.stream()
        .filter(clientSocket1 -> clientSocket1.getSocket() == socket).findFirst().orElse(null);

    if (clientSocket == null) {
      return false;
    }
    clientSocket.setClientUuid(gameStateDTO.playerUUID());

    return true;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    if (this.port == 0) {
      this.port = port;
    } else {
      throw new PortAlreadyKnownException("Port is already set!");
    }
  }

  // Deze functie is leeg, maar is wel een nodige functie.
  @Override
  public void start(int value) {

  }
}
