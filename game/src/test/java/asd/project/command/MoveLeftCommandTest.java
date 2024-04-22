package asd.project.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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

@DisplayName("Tests for MoveLeftCommand")
class MoveLeftCommandTest {

  private Game gameMock;
  private MoveLeftCommand sut;
  private World worldMock;
  private Player playerMock;
  private Position positionMock;
  private INetwork networkMock;
  private Chunk chunkMock;

  @BeforeEach
  public void beforeEach() {
    gameMock = mock(Game.class);
    sut = new MoveLeftCommand(gameMock);
    worldMock = mock(World.class);
    playerMock = mock(Player.class);
    positionMock = mock(Position.class);
    networkMock = mock(INetwork.class);
    chunkMock = mock(Chunk.class);
  }

  @Test
  @DisplayName("perform1_check if player moves one tile to the left")
  void perform1() {
    /*Arrange*/
    var expected = 99;
    var eventCaptor = ArgumentCaptor.forClass(GameStateDTO.class);
    when(gameMock.getWorld()).thenReturn(worldMock);
    when(worldMock.getPlayer()).thenReturn(playerMock);
    when(playerMock.getPosition()).thenReturn(positionMock);
    when(playerMock.getChunk()).thenReturn(chunkMock);
    when(positionMock.getX()).thenReturn(100);
    when(gameMock.getNetwork()).thenReturn(networkMock);

    /*Act*/
    sut.perform("");

    /*Assert*/
    verify(networkMock).sendGameState(eventCaptor.capture());
    var actual = eventCaptor.getValue().x();
    assertEquals(expected, actual);
  }
}
