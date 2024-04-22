package asd.project.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IUI;
import asd.project.domain.Message;
import asd.project.domain.dto.ui.UILobbyStateDTO;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.state.LobbyState;
import java.awt.Color;
import java.net.ConnectException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for JoinLobbyCommand")
class JoinLobbyCommandTest {

  private Game gameMock;
  private INetwork networkMock;
  private IUI uiMock;
  private JoinLobbyCommand sut;

  @BeforeEach
  void setUp() {
    gameMock = mock(Game.class);
    networkMock = mock(INetwork.class);
    uiMock = mock(IUI.class);
    sut = new JoinLobbyCommand(gameMock);
  }

  @Test
  @DisplayName("perform1_test if the state changes to LobbyState after performing /join game")
  void perform1() throws InvalidRoomCodeException, ConnectException {
    //Arrange
    String roomCode = "sjonniesjen sjonniesjen";
    var eventCaptorPainterState = ArgumentCaptor.forClass(UILobbyStateDTO.class);
    var eventCaptorGameState = ArgumentCaptor.forClass(LobbyState.class);

    when(gameMock.getNetwork()).thenReturn(networkMock);
    when(gameMock.getUI()).thenReturn(uiMock);
    when(networkMock.joinGame(anyString())).thenReturn(true);

    //Act
    sut.perform(roomCode);

    //Assert
    verify(uiMock).setStatePainter(eventCaptorPainterState.capture());
    verify(gameMock).setState(eventCaptorGameState.capture());
    assertDoesNotThrow(() -> networkMock.joinGame(roomCode));
  }

  @Test
  @DisplayName("perform2_test if valid arguments are given")
  void perform2() {
    //Arrange
    String roomCode = "sjonniesjen";
    when(gameMock.getUI()).thenReturn(uiMock);

    //Act
    sut.perform(roomCode);

    //Assert
    verify(uiMock).updateMessage(new Message("Invalid arguments!", Color.RED));
  }
}
