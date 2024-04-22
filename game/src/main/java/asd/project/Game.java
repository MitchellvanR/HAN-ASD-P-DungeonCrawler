package asd.project;

import asd.project.command.CommandError;
import asd.project.config.world.EntityConfig;
import asd.project.domain.World;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.domain.dto.ui.UIStartStateDTO;
import asd.project.helper.NetworkHelper;
import asd.project.helper.StorageHelper;
import asd.project.helper.UIHelper;
import asd.project.state.ActiveState;
import asd.project.state.FindNewHostState;
import asd.project.state.EndState;
import asd.project.state.StartState;
import asd.project.state.State;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

  private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
  private final IAgent agent;
  private final INetwork network;
  private final IStorage storage;
  private final IUI ui;
  private final World world;
  private final NetworkHelper networkHelper;
  private final UIHelper uiHelper;
  private final StorageHelper storageHelper;
  private final EntityConfig entityConfig;
  private State state;
  private Disposable networkSubscription;
  private Disposable uiSubscription;
  private String username;
  private String roomCode;

  public Game(IAgent agent, INetwork network, IStorage storage, IUI ui,
      IWorldGenerator worldGenerator) {
    this.agent = agent;
    this.network = network;
    this.storage = storage;
    this.ui = ui;
    this.state = new StartState(this);
    this.world = new World(worldGenerator);
    this.networkHelper = new NetworkHelper(this);
    this.uiHelper = new UIHelper(this);
    this.storageHelper = new StorageHelper(this);
    this.entityConfig = new EntityConfig(10, 50, 1);
  }

  public void start() {
    LOGGER.log(Level.INFO, "Game is gestart!");

    networkSubscription = network.getObservable().subscribe(event -> {
      switch (event.eventType()) {
        case NETWORK_GAME_STATE:
          networkHelper.sendGameStateToUI((GameStateDTO) event.object());
          if (!(state instanceof EndState)) {
            storageHelper.sendPlayerStateToDatabase(world.getPlayer());
            uiHelper.updateUIStats(world.getPlayer());
          }
          break;
        case NETWORK_MESSAGE:
          networkHelper.sendMessageToUI((MessageDTO) event.object());
          break;
        case NETWORK_CONNECTION_CHANGE:
          storageHelper.deleteAllSubhosts();
          storageHelper.sendSubHostToDatabase((SubHostListDTO) event.object());
          break;
        case NETWORK_SET_SUBHOST_LIST:
          storageHelper.setSubHostList();
          break;
        case NETWORK_GAME_START:
          networkHelper.startGame((StartGameDTO) event.object());
          break;
        case NETWORK_SET_ENTITY_CONFIG:
          entityConfig.setConfig(event.object());
          break;
        case NETWORK_REPLACE_PLAYER:
          var player = storage.getPlayerState((UUID) event.object());
          player.setGame(this);
          agent.startAgent(player);
          break;
        case NETWORK_GAME_PAUSE:
          networkHelper.pauseGame();
          storageHelper.sendGameStateToDatabase();
          break;
        case NETWORK_DISCONNECT_CLIENT:
          networkHelper.pauseGame();
          networkHelper.disconnectClient();
          break;
        case NETWORK_CONNECTION_DISCONNECT:
          storageHelper.deleteSubHost((SubHostDTO) event.object());
          break;
        case NETWORK_INTERNET_CONNECTION_ISSUES:
          ui.updateMessage(CommandError.ERROR_NO_INTERNET.toMessage());
          ui.setStatePainter(new UIStartStateDTO());
          setState(new StartState(this));
          break;
        case NETWORK_HOST_DISCONNECT:
          setState(new FindNewHostState(this));
          ui.updateMessage(CommandError.INFO_SEARCHING_HOST.toWarning());
          break;
        case NETWORK_HOST_FOUND:
          setState(new ActiveState(this));
          ui.updateMessage(CommandError.INFO_HOST_FOUND.toWarning());
          break;
        case NETWORK_SEND_MESSAGE_ISSUE:
          ui.updateMessage(CommandError.ERROR_SEND_MESSAGE.toMessage());
          break;
        default:
          LOGGER.log(Level.WARNING, String.format("Type not expected: %s",
              event.object().toString()));
          break;
      }
    });

    uiSubscription = ui.getObservable().subscribe(event -> {
      switch (event.eventType()) {
        case UI_COMMAND:
          uiHelper.runCommandFromUI((String) event.object());
          break;
        case UI_MESSAGE:
          if (!(state instanceof FindNewHostState)) {
            uiHelper.sendMessageToNetwork((String) event.object());
          } else {
            ui.updateMessage(CommandError.INFO_COMMAND_USAGE_WHILE_SEARCHING_HOST.toWarning());
          }
          break;
        default:
          LOGGER.log(Level.WARNING, String.format("Type not expected: %s",
              event.object().toString()));
          break;
      }
    });
  }

  public IUI getUI() {
    return ui;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public IStorage getStorage() {
    return storage;
  }

  public World getWorld() {
    return world;
  }

  public IAgent getAgent() {
    return agent;
  }

  public INetwork getNetwork() {
    return network;
  }

  public void unsubscribe() {
    disposeSubscription(networkSubscription);
    disposeSubscription(uiSubscription);
  }

  private void disposeSubscription(Disposable subscription) {
    if (subscription != null && !subscription.isDisposed()) {
      subscription.dispose();
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getRoomCode() {
    return roomCode;
  }

  public void setRoomCode(String roomCode) {
    this.roomCode = roomCode;
  }
}
