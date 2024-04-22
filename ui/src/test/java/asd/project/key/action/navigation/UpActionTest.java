package asd.project.key.action.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import asd.project.key.GameKeyListener;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UpAction")
class UpActionTest {

  private UpAction sut;
  private GameKeyListener gameKeyListenerMock;
  private NavigationHandler navigationHandlerMock;

  @BeforeEach
  public void beforeEach() {
    this.sut = new UpAction();
    gameKeyListenerMock = mock(GameKeyListener.class);
    navigationHandlerMock = mock(NavigationHandler.class);
  }

  @Test
  @DisplayName("perform1_check if navigation messages is empty")
  void perform1() {
    /*Arrange*/
    var expected = "test";
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);

    /*Act*/
    var actual = sut.perform(gameKeyListenerMock, "test", ' ');

    /*Assert*/
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("perform2_check if last message is reached")
  void perform2() {
    /*Arrange*/
    var expected = "test";
    var messages = new LinkedList<String>();
    messages.add("test1");
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);
    when(navigationHandlerMock.getMessages()).thenReturn(messages);

    /*Act*/
    var actual = sut.perform(gameKeyListenerMock, "test", ' ');

    /*Assert*/
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("perform3_check if next message is returned")
  void perform3() {
    /*Arrange*/
    var expected = "test1";
    var messages = new LinkedList<String>();
    messages.add("test1");
    when(gameKeyListenerMock.getNavigationHandler()).thenReturn(navigationHandlerMock);
    when(navigationHandlerMock.getMessages()).thenReturn(messages);
    when(navigationHandlerMock.getMessageIndex()).thenReturn(-1);

    /*Act*/
    var actual = sut.perform(gameKeyListenerMock, "test", ' ');

    /*Assert*/
    assertEquals(expected, actual);
  }
}
