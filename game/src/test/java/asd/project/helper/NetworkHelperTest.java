package asd.project.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IStorage;
import asd.project.IUI;
import asd.project.domain.Chunk;
import asd.project.domain.Grid;
import asd.project.domain.Position;
import asd.project.domain.World;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.dto.ui.UIActiveStateDTO;
import asd.project.domain.entity.Player;
import asd.project.state.ActiveState;
import asd.project.state.EndState;
import java.util.HashMap;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for NetworkHelper")
class NetworkHelperTest {

  private NetworkHelper sut;
  private Game gameMock;
  private IUI uiMock;
  private World worldMock;
  private Player player;
  private Chunk chunkMock;
  private int power;
  private int health;
  private int money;

  @BeforeEach
  void beforeEach() {
    gameMock = mock(Game.class);
    uiMock = mock(IUI.class);
    worldMock = mock(World.class);
    chunkMock = mock(Chunk.class);

    power = 5;
    health = 100;
    money = 10;

    sut = new NetworkHelper(gameMock);
  }

  @Test
  @DisplayName(
      "sendGameStateToUI1 - test whether player position is updated when player chunk UUID "
          + "is the same as the chunk UUID in the game state DTO")
  public void sendGameStateToUI1() {
    // Arrange
    int initialPositionX = 2;
    int initialPositionY = 2;

    int gameStateDTOPosX = 3;
    int gameStateDTOPosY = 2;

    UUID uuidMockPlayerChunk = UUID.fromString("1a9babd4-51ee-4e3c-8d9e-d5fa44dbe5bf");
    UUID uuidMockGameStateDTOChunk = UUID.fromString("1a9babd4-51ee-4e3c-8d9e-d5fa44dbe5bf");

    player = new Player(chunkMock, new Position(initialPositionX, initialPositionY),
        uuidMockPlayerChunk, power, health, money);

    when(chunkMock.getUUID()).thenReturn(uuidMockGameStateDTOChunk);
    when(chunkMock.getGrid()).thenReturn(new Grid(10, 10));

    GameStateDTO gameStateDTO = new GameStateDTO(gameStateDTOPosX, gameStateDTOPosY,
        player.getUUID(), chunkMock.getUUID(), power, health, money, 0);

    when(gameMock.getWorld()).thenReturn(worldMock);
    when(gameMock.getUI()).thenReturn(uiMock);
    when(worldMock.getPlayer()).thenReturn(player);
    when(gameMock.getState()).thenReturn(new ActiveState(gameMock));

    HashMap<UUID, Player> playerHashMap = new HashMap<>();
    playerHashMap.put(player.getUUID(), player);

    when(chunkMock.getPlayerHashMap()).thenReturn(playerHashMap);

    // Act
    sut.sendGameStateToUI(gameStateDTO);

    // Assert
    assertEquals(gameStateDTO.x(), player.getPosition().getX());
    assertEquals(gameStateDTO.y(), player.getPosition().getY());

    verify(gameMock.getUI()).setStateDTO(any(UIActiveStateDTO.class));
  }

  @Test
  @DisplayName("sendGameStateToUI2 - test whether player gets added to player hashmap when not part of hashmap yet but same UUID as chunk of GameStateDTO")
  public void sendGameStateToUI2() {
    // Arrange
    UUID uuidMockPlayerChunk = UUID.fromString("1a9babd4-51ee-4e3c-8d9e-d5fa44dbe5bf");
    UUID uuidMockGameStateDTOChunk = UUID.fromString("1a9babd4-51ee-4e3c-8d9e-d5fa44dbe5bf");

    player = new Player(chunkMock, new Position(0, 0), uuidMockPlayerChunk, power, health, money);

    when(chunkMock.getUUID()).thenReturn(uuidMockGameStateDTOChunk);

    GameStateDTO gameStateDTO = new GameStateDTO(0, 0, player.getUUID(), chunkMock.getUUID(), power,
        health, money, 0);

    when(gameMock.getWorld()).thenReturn(worldMock);
    when(gameMock.getUI()).thenReturn(uiMock);
    when(worldMock.getPlayer()).thenReturn(player);
    when(gameMock.getState()).thenReturn(new ActiveState(gameMock));

    HashMap<UUID, Player> playerHashMap = new HashMap<>();
    HashMap<UUID, Player> expectedPlayerHashMap = new HashMap<>();
    expectedPlayerHashMap.put(player.getUUID(), player);

    when(chunkMock.getPlayerHashMap()).thenReturn(playerHashMap);

    // Act
    sut.sendGameStateToUI(gameStateDTO);

    // Assert
    assertTrue(expectedPlayerHashMap.containsKey(player.getUUID()));
    verify(gameMock.getUI()).setStateDTO(any(UIActiveStateDTO.class));
  }

  @Test
  @DisplayName("sendGameStateToUI3 - test whether method returns when player UUID is different from GameStateDTO chunk UUID")
  void sendGameStateToUI3() {
    // Arrange
    UUID uuidMockPlayerChunk = UUID.fromString("52f8c489-1adc-4346-8ee6-428171aaadd8");
    UUID uuidMockGameStateDTOChunk = UUID.fromString("1a9babd4-51ee-4e3c-8d9e-d5fa44dbe5bf");

    Player playerMock = mock(Player.class);

    GameStateDTO gameStateDTO = new GameStateDTO(0, 0, null, uuidMockGameStateDTOChunk, power,
        health, money, 0);

    when(gameMock.getWorld()).thenReturn(worldMock);
    when(worldMock.getPlayer()).thenReturn(playerMock);
    when(playerMock.getChunk()).thenReturn(chunkMock);
    when(chunkMock.getUUID()).thenReturn(uuidMockPlayerChunk);

    // Act
    sut.sendGameStateToUI(gameStateDTO);

    // Assert
    verify(chunkMock, times(0)).getPlayerHashMap();
  }

  @Test
  @DisplayName("startGame1_test whether method calls setStatePainter with the parameter")
  public void startGame1() {
    // Arrange
    var startGameDTO = new StartGameDTO(40, 40, 40, UUID.randomUUID());
    var playerMock = mock(Player.class);

    mockConstruction(World.class, (mock, context) -> {
      when(mock.getPlayer()).thenReturn(playerMock);
    });

    when(gameMock.getUI()).thenReturn(uiMock);
    when(gameMock.getWorld()).thenReturn(worldMock);
    when(worldMock.expand(any(UUID.class))).thenReturn(chunkMock);
    when(worldMock.initializePlayer(any(Chunk.class))).thenReturn(playerMock);

    // Act
    sut.startGame(startGameDTO);

    // Assert
    verify(uiMock).setStatePainter(new UIActiveStateDTO(playerMock));
  }

  @Test
  @DisplayName("handlePlayerDeath1_test whether death is handled and the player wins")
  public void handlePlayerDeath1() {
    // Arrange
    var gameStateDTO = new GameStateDTO(0, 0, UUID.randomUUID(), null, power, 0, money, 0);
    var playerMock = mock(Player.class);

    when(gameMock.getWorld()).thenReturn(worldMock);
    when(worldMock.getPlayer()).thenReturn(playerMock);
    when(playerMock.getChunk()).thenReturn(chunkMock);
    when(chunkMock.getPlayerHashMap()).thenReturn(new HashMap<>());

    var storageMock = mock(IStorage.class);
    when(gameMock.getStorage()).thenReturn(storageMock);
    doNothing().when(storageMock).deletePlayer(any());
    when(storageMock.getAmountOfKnightsAlive()).thenReturn(1);
    when(gameMock.getState()).thenReturn(new ActiveState(gameMock));

    var networkMock = mock(INetwork.class);
    when(gameMock.getNetwork()).thenReturn(networkMock);
    doNothing().when(networkMock).sendMessage(any());
    when(gameMock.getUI()).thenReturn(uiMock);
    when(gameMock.getState()).thenReturn(new ActiveState(gameMock));
    when(gameMock.getUI()).thenReturn(uiMock);

    //Act
    sut.handlePlayerDeath(gameStateDTO);

    //Assert
    verify(gameMock).setState(any(EndState.class));

  }
}