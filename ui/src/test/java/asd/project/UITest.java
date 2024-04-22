package asd.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.key.GameKeyListener;
import asd.project.key.action.EnterAction;
import asd.project.key.action.navigation.NavigationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("Tests for UI")
class UITest {

  private UI uiMock;

  @BeforeEach
  void setUp() {
    uiMock = mock(UI.class);
  }

  @Test
  @DisplayName("perform1_tests whether the published is with EventType UiCommand when the EnterAction is used with a command starting with a slash.")
  void perform1() {
    // Arrange
    EnterAction enterAction = new EnterAction();
    GameKeyListener gameKeyListenerMock = mock(GameKeyListener.class);
    String characters = "/start game";

    when(gameKeyListenerMock.getUI()).thenReturn(uiMock);
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(mock(NavigationHandler.class));

    // Act
    enterAction.perform(gameKeyListenerMock, characters, characters.charAt(0));

    // Assert
    ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
    verify(uiMock).publish(eventCaptor.capture());

    Event capturedEvent = eventCaptor.getValue();
    EventType eventType = capturedEvent.eventType();

    assertEquals(EventType.UI_COMMAND, eventType);
  }

  @Test
  @DisplayName("perform2_tests whether the published is with EventType UiMessage when the EnterAction is used with a command starting with a slash.")
  void perform2() {
    // Arrange
    EnterAction enterAction = new EnterAction();
    GameKeyListener gameKeyListenerMock = mock(GameKeyListener.class);
    String characters = "hoi";

    when(gameKeyListenerMock.getUI()).thenReturn(uiMock);
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(mock(NavigationHandler.class));

    // Act
    enterAction.perform(gameKeyListenerMock, characters, characters.charAt(0));

    // Assert
    ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
    verify(uiMock).publish(eventCaptor.capture());

    Event capturedEvent = eventCaptor.getValue();
    EventType eventType = capturedEvent.eventType();

    assertEquals(EventType.UI_MESSAGE, eventType);
  }
}
