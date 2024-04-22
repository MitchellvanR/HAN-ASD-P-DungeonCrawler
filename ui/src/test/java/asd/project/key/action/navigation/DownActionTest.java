package asd.project.key.action.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.key.GameKeyListener;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for DownAction")
class DownActionTest {

  private DownAction sut;
  private GameKeyListener gameKeyListenerMock;
  private NavigationHandler navigationHandlerMock;

  @BeforeEach
  void beforeEach() {
    sut = new DownAction();
    gameKeyListenerMock = mock(GameKeyListener.class);
    navigationHandlerMock = mock(NavigationHandler.class);
  }

  @Test
  @DisplayName("perform1_tests whether when down action gets pressed when older messages are available earlier message gets returned")
  void perform1() {
    // Arrange
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);

    int initialMessageIndex = 2;
    int expectedMessageIndex = 1;
    String expectedMessage = "expected";
    when(navigationHandlerMock.getMessageIndex()).thenReturn(initialMessageIndex);

    LinkedList<String> messages = new LinkedList<>();
    messages.add("test");
    messages.add(expectedMessage);
    when(navigationHandlerMock.getMessages()).thenReturn(messages);

    // Act
    String result = sut.perform(gameKeyListenerMock, "down", ' ');

    // Assert
    assertEquals(expectedMessage, result);
    verify(navigationHandlerMock).setMessageIndex(expectedMessageIndex);
  }

  @Test
  @DisplayName("perform2_tests whether when down action gets pressed when no older messages are available current message gets returned")
  void perform2() {
    // Arrange
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);

    int initialMessageIndex = 0;
    String expectedMessage = "expected";
    when(navigationHandlerMock.getMessageIndex()).thenReturn(initialMessageIndex);

    LinkedList<String> messages = new LinkedList<>();
    when(navigationHandlerMock.getMessages()).thenReturn(messages);

    // Act
    String result = sut.perform(gameKeyListenerMock, expectedMessage, ' ');

    // Assert
    assertEquals(expectedMessage, result);
  }
}