package asd.project.unittest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.domain.Chunk;
import asd.project.domain.dto.storage.StorageGameStateDTO;
import asd.project.domain.dto.storage.StorageWorldConfiguration;
import asd.project.domain.entity.Player;
import asd.project.sqlite.SQLiteStorage;
import asd.project.sqlite.dao.PlayerDAO;
import asd.project.sqlite.dao.savegame.SaveGame;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

@DisplayName("Test for Storage class")
class SQLiteStorageTest {

  private SQLiteStorage sut;
  private MockedConstruction<PlayerDAO> playerDAOMock;
  private MockedConstruction<SaveGame> saveGameMock;

  @BeforeEach
  void beforeMethod() {
    playerDAOMock = mockConstruction(PlayerDAO.class);
    saveGameMock = mockConstruction(SaveGame.class);
    sut = new SQLiteStorage();
  }

  @AfterEach
  void afterMethod() {
    playerDAOMock.close();
    saveGameMock.close();
  }

  @Test
  @DisplayName("savePlayerState1_check if player state gets saved")
  void savePlayerState1() {
    // Arrange
    PlayerDAO playerDAO = playerDAOMock.constructed().get(0);

    Player player = new Player(new Chunk(200, 200, 10, UUID.randomUUID()));
    when(playerDAO.getAllPlayers()).thenReturn(java.util.Collections.emptyList());

    // Act
    sut.savePlayerState(player);

    // Assert
    verify(playerDAO, times(1)).addPlayer(player);
    verify(playerDAO, never()).updateAllPlayersInChunk(any());
  }

  @Test
  @DisplayName("savePlayerState2_check whether player gets updated if list is not empty")
  void savePlayerState2() {
    // Arrange
    PlayerDAO playerDAO = playerDAOMock.constructed().get(0);
    Player player = new Player(new Chunk(200, 200, 10, UUID.randomUUID()));

    when(playerDAO.getAllPlayers()).thenReturn(
        java.util.Collections.singletonList(
            new Player(new Chunk(200, 200, 10, UUID.randomUUID()))));

    doNothing().when(playerDAO).addPlayer(player);
    doNothing().when(playerDAO).updateAllPlayersInChunk(player);

    // Act
    sut.savePlayerState(player);

    // Assert
    verify(playerDAO, never()).addPlayer(any());
    verify(playerDAO, times(1)).updateAllPlayersInChunk(player);
  }

  @Test
  @DisplayName("sendGameStateToDatabase1_test if create file of saved game is executed")
  void sendGameStateToDatabase1() {
    //Arrange
    var chunk = new Chunk(10, 10, 0, UUID.randomUUID());
    var player = new Player(chunk);
    var storageGameStateDTO = new StorageGameStateDTO(player);
    var worldConfiguration = new StorageWorldConfiguration(10, "hard");
    var roomCode = "1234";

    //Act
    sut.sendGameStateToDatabase(storageGameStateDTO, worldConfiguration, roomCode);

    //Assert
    verify(saveGameMock.constructed().get(0)).createFileOfSavedGame(roomCode, player,
        worldConfiguration);
  }
}
