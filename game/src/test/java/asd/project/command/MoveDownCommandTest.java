package asd.project.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.World;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for MoveDownCommand")
class MoveDownCommandTest {

  private MoveDownCommand sut;
  private INetwork networkMock;
  private Game gameMock;
  private World worldMock;
  private Player playerMock;
  private Position positionMock;
  private Chunk chunkMock;

  @BeforeEach
  void beforeEach() {
    networkMock = mock(INetwork.class);
    gameMock = mock(Game.class);

    worldMock = mock(World.class);
    playerMock = mock(Player.class);
    positionMock = mock(Position.class);
    chunkMock = mock(Chunk.class);

    sut = new MoveDownCommand(gameMock);
  }

  @Test
  @DisplayName("perform1_test whether move down command triggers expected y to be 1 higher")
  void perform1() {
    // Arrange
    int initialY = 100;
    int expectedY = 101;

    ArgumentCaptor<GameStateDTO> eventCaptor = ArgumentCaptor.forClass(GameStateDTO.class);

    when(gameMock.getWorld()).thenReturn(worldMock);
    when(worldMock.getPlayer()).thenReturn(playerMock);
    when(playerMock.getPosition()).thenReturn(positionMock);
    when(playerMock.getChunk()).thenReturn(chunkMock);
    when(gameMock.getNetwork()).thenReturn(networkMock);

    when(positionMock.getY()).thenReturn(initialY);

    // Act
    sut.perform("");

    // Assert
    verify(networkMock, times(1)).sendGameState(any(GameStateDTO.class));
    verify(networkMock).sendGameState(eventCaptor.capture());
    int actual = eventCaptor.getValue().y();
    assertEquals(expectedY, actual);
  }
}
