package asd.project.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.IUI;
import asd.project.domain.dto.ui.UIHostGameStateDTO;
import asd.project.state.HostGameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

@DisplayName("Tests for HostGameCommand")
class HostGameCommandTest {

  private MockedConstruction<UIHostGameStateDTO> uiHostGameStateDTOMock;
  private MockedConstruction<HostGameState> hostGameStateMock;

  private Game gameMock;
  private IUI UIMock;

  private HostGameCommand sut;

  @BeforeEach
  void beforeEach() {
    uiHostGameStateDTOMock = mockConstruction(UIHostGameStateDTO.class);
    hostGameStateMock = mockConstruction(HostGameState.class);

    gameMock = mock(Game.class);
    UIMock = mock(IUI.class);

    sut = new HostGameCommand(gameMock);

    when(gameMock.getUI()).thenReturn(UIMock);
  }

  @AfterEach
  void afterEach() {
    uiHostGameStateDTOMock.close();
    hostGameStateMock.close();
  }

  @Test
  @DisplayName("perform1_Check if the StatePainter gets changed to an instance of HostGameStateDTO")
  void perform1() {
    // Act
    sut.perform("");

    // Assert
    verify(UIMock).setStatePainter(any(UIHostGameStateDTO.class));
  }

  @Test
  @DisplayName("perform2_Check if the State gets changed to an instance of HostGameState")
  void perform2() {
    // Act
    sut.perform("");

    // Assert
    verify(gameMock).setState(any(HostGameState.class));
  }
}
