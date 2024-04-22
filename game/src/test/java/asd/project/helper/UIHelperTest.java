package asd.project.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IStorage;
import asd.project.IUI;
import asd.project.command.StartGameCommand;
import asd.project.domain.Message;
import asd.project.domain.Position;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.entity.Player;
import asd.project.state.State;
import java.awt.Color;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for UIHelper")
class UIHelperTest {

  private Game gameMock;
  private UIHelper sut;
  private State stateMock;
  private IUI uiMock;
  private StartGameCommand startGameCommandMock;

  @BeforeEach
  void beforeEach() {
    gameMock = mock(Game.class);
    sut = new UIHelper(gameMock);
    stateMock = mock(State.class);
    uiMock = mock(IUI.class);
    startGameCommandMock = mock(StartGameCommand.class);
  }

  @Test
  @DisplayName("runCommandFromUI1_check if command not gets executed when it not exists")
  void runCommandFromUI1() {
    // Arrange
    var expected = new Message("The command doesn't exist!", Color.RED);
    var eventCapture = ArgumentCaptor.forClass(Message.class);
    when(gameMock.getState()).thenReturn(stateMock);
    when(stateMock.getCommandFromList("/doesn't exist")).thenReturn(Optional.empty());
    when(gameMock.getUI()).thenReturn(uiMock);

    // Act
    sut.runCommandFromUI("/doesn't exist");

    // Assert
    verify(uiMock).updateMessage(eventCapture.capture());
    var actual = eventCapture.getValue();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("runCommandFromUI2_check if command gets executed when it exists")
  void runCommandFromUI2() {
    // Arrange
    when(gameMock.getState()).thenReturn(stateMock);
    when(stateMock.getCommandFromList("/start game")).thenReturn(Optional.of(startGameCommandMock));
    when(startGameCommandMock.getArgument("/start game")).thenReturn("");

    // Act
    sut.runCommandFromUI("/start game");

    // Assert
    verify(startGameCommandMock).perform("");
  }

  @Test
  @DisplayName("updateUIStats1_check if player death gets noticed and methods are called")
  public void updateUIStats1() {
    // Arrange
    var playerMock = mock(Player.class);
    when(playerMock.getHealth()).thenReturn(0);
    when(gameMock.getUI()).thenReturn(uiMock);

    var sutSpy = spy(sut);
    doNothing().when(sutSpy).handlePlayerDeath(playerMock);

    // Act
    sutSpy.updateUIStats(playerMock);

    // Assert
    verify(uiMock).updateUserStats(playerMock);
  }

  @Test
  @DisplayName("handlePlayerDeath1_check if player death gets handled correctly")
  public void handlePlayerDeath1() {
    // Arrange
    var player = new Player(new Position(0, 0), UUID.randomUUID(), 1, 0, 0);
    var storageMock = mock(IStorage.class);
    when(gameMock.getStorage()).thenReturn(storageMock);
    when(gameMock.getUI()).thenReturn(uiMock);
    var networkMock = mock(INetwork.class);
    when(gameMock.getNetwork()).thenReturn(networkMock);
    when(storageMock.getAmountOfKnightsAlive()).thenReturn(2);
    when(gameMock.getUsername()).thenReturn("test");

    // Act
    sut.handlePlayerDeath(player);

    // Assert
    verify(networkMock).sendMessage(any(MessageDTO.class));
  }
}
