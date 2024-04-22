package asd.project;

import asd.project.client.Client;
import asd.project.command.CommandError;
import asd.project.config.world.EntityConfig;
import asd.project.domain.Room;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.dto.GameCodeFileDto;
import asd.project.exceptions.ClientGameStartException;
import asd.project.exceptions.CreateRoomException;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.server.Server;
import asd.project.services.IDropboxService;
import com.google.inject.Inject;
import java.awt.Color;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Network extends EventObservable implements INetwork {

  private static final int PORT_TIMEOUT = 5000;
  private static final Logger LOGGER = Logger.getLogger(Network.class.getName());

  private final IDropboxService dropboxService;
  private NetworkStructure networkStructure;
  private SubHostListDTO subHostListDTO;

  private int port = 0;

  @Inject
  public Network(IDropboxService dropboxService) {
    this.dropboxService = dropboxService;
  }

  @Override
  public String createRoom() throws CreateRoomException {
    Server server = new Server(this);

    networkStructure = server;
    startNetworkStructure();

    if (port != 0) {
      server.setPort(port);
    } else {
      long start = System.currentTimeMillis();
      long end = start + PORT_TIMEOUT;
      while (server.getPort() == 0) {
        if (System.currentTimeMillis() > end) {
          throw new CreateRoomException(CommandError.ERROR_CREATE_ROOM.toString());
        }
      }
      port = server.getPort();
    }

    Room room;
    try {
      room = new Room(InetAddress.getLocalHost().getHostAddress(), port);
    } catch (UnknownHostException exception) {
      String exceptionMessage = String.format("Exception thrown in Network.createRoom: %s",
          exception.getMessage());
      LOGGER.log(Level.SEVERE, exceptionMessage);
      return null;
    }
    var roomCode = dropboxService.uploadRoom(room, true);
    if (!dropboxService.isCanConnect()){
      fireEvent(EventType.NETWORK_INTERNET_CONNECTION_ISSUES, "");
      return null;
    }
    return roomCode;
  }

  public String createRoom(int port) throws CreateRoomException {
    this.port = port;
    return createRoom();
  }

  @Override
  public boolean joinGame(String code) throws ConnectException, InvalidRoomCodeException {
    GameCodeFileDto gameCodeFileDto = dropboxService.getGamecodeFileDto(code, true);
    if (!dropboxService.isCanConnect()){
      fireEvent(EventType.NETWORK_INTERNET_CONNECTION_ISSUES, "");
      return false;
    }else {
      connectToHost(gameCodeFileDto);
      return true;
    }
  }

  public void joinGameWhenHostDisconnect(String code, int value)
      throws InvalidRoomCodeException, ConnectException {
    GameCodeFileDto gameCodeFileDto = dropboxService.getGamecodeFileDto(code, true);
    if (!dropboxService.isCanConnect()){
      fireEvent(EventType.NETWORK_INTERNET_CONNECTION_ISSUES, "");
    }else {
      if (gameCodeFileDto.ip() != null) {
        try {
          networkStructure = new Client(this, gameCodeFileDto.ip(), gameCodeFileDto.port());
          networkStructure.start(value);
        } catch (RuntimeException r) {
          throw new ConnectException();
        }
      } else {
        throw new InvalidRoomCodeException("Invalid room code was filled in!");
      }
    }
  }

  private void connectToHost(GameCodeFileDto gameCodeFileDto)
      throws ConnectException, InvalidRoomCodeException {

    if (gameCodeFileDto != null) {
      try {
        networkStructure = new Client(this, gameCodeFileDto.ip(), gameCodeFileDto.port());
        startNetworkStructure();
      } catch (RuntimeException r) {
        throw new ConnectException();
      }
    } else {
      throw new InvalidRoomCodeException(CommandError.ERROR_INVALID_CODE.toString());
    }
  }

  @Override
  public void fireEvent(EventType eventType, Object object) {
    Event event = new Event(eventType, object);
    publish(event);
  }

  @Override
  public void startGame(StartGameDTO startGameDTO) throws ClientGameStartException {
    if (networkStructure instanceof Server server) {
      server.handleGameStart(startGameDTO);
    } else {
      throw new ClientGameStartException(CommandError.ERROR_CLIENT_STARTGAME.toString());
    }
  }

  public void pauseGame() {
    networkStructure.handlePauseGame();
  }

  @Override
  public void leaveGame() {
    networkStructure.leave();
  }

  @Override
  public void sendGameState(GameStateDTO gameStateDTO) {
    networkStructure.handleOutput(gameStateDTO);
  }

  @Override
  public void sendMessage(MessageDTO messageDTO) {
    if (networkStructure == null) {
      var event = new Event(EventType.NETWORK_MESSAGE,
          new MessageDTO("You are not connected to a host.", Color.RED));

      publish(event);

      return;
    }
    networkStructure.handleOutput(messageDTO);
  }

  protected Server createServer() {
    return new Server(this);
  }

  protected Client createClient(String ip, int port) {
    return new Client(this, ip, port);
  }
  @Override
  public void setSubHostList(SubHostListDTO subHostListDTO) {
    this.subHostListDTO = subHostListDTO;
  }

  @Override
  public void setEntityConfig(EntityConfig entityConfig) {
    if (networkStructure != null) {
      networkStructure.handleSetEntityConfig(entityConfig);
    }
  }

  public SubHostListDTO getSubHostList() {
      return subHostListDTO;
  }

  public void startNetworkStructure() {
      new Thread(networkStructure::start).start();
  }

  @Override
  public String getRoomCode() {
    return dropboxService.getRoomCode();
  }

  @Override
  public String openServer() throws CreateRoomException {
    networkStructure = new Server(this);
    String roomCode = createRoom();
    if (roomCode != null) {
      startNetworkStructure();
    }
    return roomCode;
  }

  public IDropboxService getDropboxService() {
    return dropboxService;
  }

  public void setNetworkStructure(NetworkStructure networkStructure) {
    this.networkStructure = networkStructure;
  }

  public void setRoomCode(String roomCode) {
    dropboxService.setRoomCode(roomCode);
  }

}
