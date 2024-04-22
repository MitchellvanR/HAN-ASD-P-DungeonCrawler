package asd.project.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.domain.dto.ui.UILobbyStateDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

@DisplayName("Tests for PauseGameCommand")
class PauseGameCommandTest {

  INetwork networkMock;
  private MockedConstruction<UILobbyStateDTO> uiLobbyStateDTOMock;
  private PauseGameCommand sut;

  @BeforeEach
  public void beforeEach() {
    uiLobbyStateDTOMock = mockConstruction(UILobbyStateDTO.class);
    Game gameMock = mock(Game.class);
    networkMock = mock(INetwork.class);

    sut = new PauseGameCommand(gameMock);
    when(gameMock.getNetwork()).thenReturn(networkMock);
  }

  @AfterEach
  public void afterEach() {
    uiLobbyStateDTOMock.close();
  }

  @Test
  @DisplayName("perform1_Check if Network.pauseGame gets called")
  void perform1() {
    // Act
    sut.perform("perform1_Check");

    // Assert
    verify(networkMock).pauseGame();
  }
}
