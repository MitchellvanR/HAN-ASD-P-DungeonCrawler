package asd.project.integrationtest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.IAgent;
import asd.project.IStorage;
import asd.project.IUI;
import asd.project.IWorldGenerator;
import asd.project.Network;
import asd.project.NetworkStructure;
import asd.project.client.Client;
import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.serializable.DisconnectClientDTO;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.PauseGameDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.entity.Player;
import asd.project.domain.event.Event;
import asd.project.helper.NetworkHelper;
import asd.project.helper.StorageHelper;
import asd.project.helper.UIHelper;
import asd.project.services.DropboxService;
import io.reactivex.rxjava3.core.Observable;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

@Tag("integration-test")
@DisplayName("Integration-test-NetworkStructure")
public class NetworkStructureIT {

  private MockedConstruction<NetworkHelper> networkHelperMockedConstruction;
  private MockedConstruction<StorageHelper> storageHelperMockedConstruction;
  private MockedConstruction<UIHelper> uiHelperMockedConstruction;
  private MockedConstruction<EntityConfig> entityConfigMockedConstruction;

  private IUI iUIMock;
  private Player playerMock;
  private IStorage iStorageMock;
  private IAgent agentMock;

  private Network network;
  private NetworkStructure sut;

  private Game game;

  @BeforeEach
  void beforeEach() {
    networkHelperMockedConstruction = mockConstruction(NetworkHelper.class);
    storageHelperMockedConstruction = mockConstruction(StorageHelper.class);
    uiHelperMockedConstruction = mockConstruction(UIHelper.class);
    entityConfigMockedConstruction = mockConstruction(EntityConfig.class);

    iUIMock = mock(IUI.class);
    when(iUIMock.getObservable()).thenReturn(mock(Observable.class));
    playerMock = mock(Player.class);
    iStorageMock = mock(IStorage.class);
    when(iStorageMock.getPlayerState(any(UUID.class))).thenReturn(playerMock);
    agentMock = mock(IAgent.class);

    network = new Network(mock(DropboxService.class));
    sut = new Client(network, "0.0.0.0", 0);

    game = new Game(agentMock, network, iStorageMock, iUIMock, mock(IWorldGenerator.class));
    game.start();
  }

  @AfterEach
  void afterEach() {
    networkHelperMockedConstruction.close();
    storageHelperMockedConstruction.close();
    uiHelperMockedConstruction.close();
    entityConfigMockedConstruction.close();
  }

  @Test
  @DisplayName("gameStateDTOTest1_test if correct functions are called")
  void gameStateDTOTest1() {
    // Arrange
    GameStateDTO gameState = new GameStateDTO(0, 0, UUID.randomUUID(), UUID.randomUUID(), 0, 0, 0,
        0);
    Player player = game.getWorld().getPlayer();

    // Act
    Event event = sut.createEvent(gameState);
    network.publish(event);

    // Assert
    verify(networkHelperMockedConstruction.constructed().get(0)).sendGameStateToUI(gameState);
    verify(storageHelperMockedConstruction.constructed().get(0)).sendPlayerStateToDatabase(player);
    verify(uiHelperMockedConstruction.constructed().get(0)).updateUIStats(player);
  }

  @Test
  @DisplayName("messageDTOTest1_test if correct functions are called")
  void messageDTOTest1() {
    // Arrange
    MessageDTO message = new MessageDTO("");

    // Act
    Event event = sut.createEvent(message);
    network.publish(event);

    // Assert
    verify(networkHelperMockedConstruction.constructed().get(0)).sendMessageToUI(message);
  }

  @Test
  @DisplayName("startGameDTOTest1_test if correct functions are called")
  void startGameDTOTest1() {
    // Arrange
    StartGameDTO startGame = new StartGameDTO(0, 0, 0, UUID.randomUUID());

    // Act
    Event event = sut.createEvent(startGame);
    network.publish(event);

    // Assert
    verify(networkHelperMockedConstruction.constructed().get(0)).startGame(startGame);
  }

  @Test
  @DisplayName("entityConfigTest1_test if correct functions are called")
  void entityConfigTest1() throws Exception {
    // Arrange
    Callable<Long> getInvocationCount = () -> entityConfigMockedConstruction.constructed().stream()
        .map(mock ->
            mockingDetails(mock).getInvocations()
                .stream()
                .filter(invocation -> invocation.getMethod().getName().equals("setConfig"))
                .count()
        ).reduce(Long::sum).orElse(0L);

    EntityConfig entityConfig = new EntityConfig();
    long invocationCounterBefore = getInvocationCount.call();

    // Act
    Event event = sut.createEvent(entityConfig);
    network.publish(event);

    // Assert
    long invocationCounterAfter = getInvocationCount.call();
    Assertions.assertNotEquals(invocationCounterBefore, invocationCounterAfter);
  }

  @Test
  @DisplayName("uuidTest1_test if correct functions are called")
  void uuidTest1() {
    // Arrange
    UUID uuid = UUID.randomUUID();

    // Act
    Event event = sut.createEvent(uuid);
    network.publish(event);

    // Assert
    verify(iStorageMock).getPlayerState(uuid);
    verify(playerMock).setGame(game);
    verify(agentMock).startAgent(playerMock);
  }

  @Test
  @DisplayName("pauseGameDTOTest1_test if correct functions are called")
  void pauseGameDTOTest1() {
    // Arrange
    PauseGameDTO pauseGame = new PauseGameDTO();

    // Act
    Event event = sut.createEvent(pauseGame);
    network.publish(event);

    // Assert
    verify(networkHelperMockedConstruction.constructed().get(0)).pauseGame();
    verify(storageHelperMockedConstruction.constructed().get(0)).sendGameStateToDatabase();
  }

  @Test
  @DisplayName("disconnectClientDTOTest1_test if correct functions are called")
  void disconnectClientDTOTest1() {
    // Arrange
    DisconnectClientDTO disconnectClient = new DisconnectClientDTO();

    // Act
    Event event = sut.createEvent(disconnectClient);
    network.publish(event);

    // Assert
    verify(networkHelperMockedConstruction.constructed().get(0)).pauseGame();
    verify(networkHelperMockedConstruction.constructed().get(0)).disconnectClient();
  }
}
