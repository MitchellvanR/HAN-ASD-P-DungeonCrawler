package asd.project.key.action;

import static asd.project.domain.event.EventType.UI_COMMAND;
import static asd.project.domain.event.EventType.UI_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.UI;
import asd.project.domain.event.Event;
import asd.project.key.GameKeyListener;
import asd.project.key.action.navigation.NavigationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for EnterActionTest")
class EnterActionTest {

  private EnterAction sut;
  private GameKeyListener gameKeyListenerMock;
  private UI uiMock;
  private NavigationHandler navigationHandlerMock;

  @BeforeEach
  public void beforeEach() {
    this.sut = new EnterAction();
    gameKeyListenerMock = mock(GameKeyListener.class);
    uiMock = mock(UI.class);
    navigationHandlerMock = mock(NavigationHandler.class);
  }

  @Test
  @DisplayName("perform1_check if characters is empty")
  void perform1() {
    // Arrange
    var expected = "";

    // Act
    var actual = sut.perform(gameKeyListenerMock, "", ' ');

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("perform2_check if characters is blank")
  void perform2() {
    // Arrange
    var expected = " ";

    // Act
    var actual = sut.perform(gameKeyListenerMock, " ", ' ');

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("perform3_check if characters is command")
  void perform3() {
    // Arrange
    var eventCaptor = ArgumentCaptor.forClass(Event.class);
    when(gameKeyListenerMock.getUI()).thenReturn(uiMock);
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);

    // Act
    sut.perform(gameKeyListenerMock, "/test", ' ');

    // Assert
    verify(uiMock).publish(eventCaptor.capture());
    var actual = eventCaptor.getValue().eventType();
    assertEquals(UI_COMMAND, actual);
  }

  @Test
  @DisplayName("perform4_check if characters is message")
  void perform4() {
    // Arrange
    var eventCaptor = ArgumentCaptor.forClass(Event.class);
    when(gameKeyListenerMock.getUI()).thenReturn(uiMock);
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);

    // Act
    sut.perform(gameKeyListenerMock, "test", ' ');

    // Assert
    verify(uiMock).publish(eventCaptor.capture());
    var actual = eventCaptor.getValue().eventType();
    assertEquals(UI_MESSAGE, actual);
  }

  @Test
  @DisplayName("perform5_check if characters is cleared")
  void perform5() {
    // Arrange
    var expected = "";
    when(gameKeyListenerMock.getUI()).thenReturn(uiMock);
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);

    // Act
    var actual = sut.perform(gameKeyListenerMock, "/test", ' ');

    // Assert
    assertEquals(expected, actual);
  }
}
