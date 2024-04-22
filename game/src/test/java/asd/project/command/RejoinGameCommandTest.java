package asd.project.command;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IUI;
import asd.project.domain.Chunk;
import asd.project.domain.World;
import asd.project.domain.dto.ui.UIActiveStateDTO;
import asd.project.domain.entity.Player;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.state.ActiveState;
import java.net.ConnectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for RejoinGameCommand")
class RejoinGameCommandTest {

  private RejoinGameCommand sut;

  private Game gameMock;
  private INetwork networkMock;
  private IUI uiMock;
  private World worldMock;
  private Player playerMock;
  private Chunk chunkMock;

  @BeforeEach
  public void beforeEach() {
    this.gameMock = mock(Game.class);
    this.networkMock = mock(INetwork.class);
    this.uiMock = mock(IUI.class);
    this.worldMock = mock(World.class);
    this.playerMock = mock(Player.class);
    this.chunkMock = mock(Chunk.class);
    this.sut = new RejoinGameCommand(gameMock);
  }

  @Test
  @DisplayName("perform1_test whether the state changes to StartState when the /rejoin with valid gameCode gets called")
  void perform1() throws ConnectException, InvalidRoomCodeException {
    // Arrange
    String argument = "testpersoon1";
    var eventCaptorPainterState = ArgumentCaptor.forClass(UIActiveStateDTO.class);
    var eventCaptorGameState = ArgumentCaptor.forClass(ActiveState.class);
    when(gameMock.getNetwork()).thenReturn(networkMock);
    networkMock.joinGame(argument);
    when(gameMock.getUI()).thenReturn(uiMock);
    when(gameMock.getWorld()).thenReturn(worldMock);
    when(worldMock.getPlayer()).thenReturn(playerMock);
    when(playerMock.getChunk()).thenReturn(chunkMock);
    when(networkMock.joinGame(anyString())).thenReturn(true);

    // Act
    sut.perform(argument);

    // Assert
    verify(uiMock).setStatePainter(eventCaptorPainterState.capture());
    verify(gameMock).setState(eventCaptorGameState.capture());
  }

}
