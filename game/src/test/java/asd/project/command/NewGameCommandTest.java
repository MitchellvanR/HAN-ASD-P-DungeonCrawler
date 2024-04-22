package asd.project.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IUI;
import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.ui.UILobbyStateDTO;
import asd.project.exceptions.CreateRoomException;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.state.LobbyState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.net.ConnectException;

@DisplayName("Tests for NewGameCommand")
class NewGameCommandTest {

  private MockedConstruction<UILobbyStateDTO> uiLobbyStateDTOMock;
  private MockedConstruction<LobbyState> lobbyStateMock;

  private Game gameMock;
  private INetwork networkMock;
  private IUI UIMock;

  private EntityConfig entityConfigMock;
  private NewGameCommand sut;

  @BeforeEach
  void beforeEach() throws InvalidRoomCodeException, ConnectException, CreateRoomException {
    uiLobbyStateDTOMock = mockConstruction(UILobbyStateDTO.class);
    lobbyStateMock = mockConstruction(LobbyState.class);

    gameMock = mock(Game.class);
    networkMock = mock(INetwork.class);
    UIMock = mock(IUI.class);
    entityConfigMock = mock(EntityConfig.class);

    sut = new NewGameCommand(gameMock);
    sut.setEntityConfig(entityConfigMock);

    when(gameMock.getNetwork()).thenReturn(networkMock);
    when(gameMock.getUI()).thenReturn(UIMock);
    when(networkMock.openServer()).thenReturn("XXXXX");
  }

  @AfterEach
  void afterEach() {
    uiLobbyStateDTOMock.close();
    lobbyStateMock.close();
  }

  @Test
  @DisplayName("perform1_Check if Network.createRoom gets called")
  void perform1() throws CreateRoomException {
    // Act
    doNothing().when(entityConfigMock).setConfig(mock(EntityConfig.class));
    sut.perform("perform1_Check");

    // Assert
    verify(networkMock).openServer();
  }

  @Test
  @DisplayName("perform2_Check if Network.createRoom gets called with the correct argument")
  void perform2() throws CreateRoomException {
    // Arrange
    String argument = "perform2_Check";

    // Act
    sut.perform(argument);

    // Assert
    verify(networkMock).openServer();
  }

  @Test
  @DisplayName("perform3_Check if the StatePainter gets changed to an instance of HostGameStateDTO")
  void perform3() {
    // Act
    sut.perform("perform3_Check");

    // Assert
    verify(UIMock).setStatePainter(any(UILobbyStateDTO.class));
  }

  @Test
  @DisplayName("perform4_Check if the State gets changed to an instance of HostGameState")
  void perform4() {
    // Act
    sut.perform("perform4_Check");

    // Assert
    verify(gameMock).setState(any(LobbyState.class));
  }

  @Test
  @DisplayName("perform5_Check if the perform returns null when it gets called with a empty argument")
  void perform5() {
    // Act
    sut.perform("");

    // Assert
    verify(gameMock, never()).getNetwork();
  }
}
