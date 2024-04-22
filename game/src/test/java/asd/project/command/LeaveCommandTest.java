package asd.project.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IUI;
import asd.project.domain.dto.ui.UIStartStateDTO;
import asd.project.state.StartState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for LeaveCommand")
class LeaveCommandTest {

  private LeaveCommand sut;
  private Game gameMock;
  private INetwork networkMock;
  private IUI uiMock;

  @BeforeEach
  void beforeEach() {
    gameMock = mock(Game.class);
    networkMock = mock(INetwork.class);
    uiMock = mock(IUI.class);

    sut = new LeaveCommand(gameMock);
  }

  @Test
  @DisplayName("assertLeave1_tests if a player can leave.")
  void assertLeave1() {
    // Arrange
    var eventCaptorPainterState = ArgumentCaptor.forClass(UIStartStateDTO.class);
    var eventCaptorGameState = ArgumentCaptor.forClass(StartState.class);

    when(gameMock.getNetwork()).thenReturn(networkMock);
    when(gameMock.getUI()).thenReturn(uiMock);

    // Act
    sut.perform(null);

    // Assert
    verify(networkMock).leaveGame();
    verify(uiMock).setStatePainter(eventCaptorPainterState.capture());
    verify(gameMock).setState(eventCaptorGameState.capture());
  }
}