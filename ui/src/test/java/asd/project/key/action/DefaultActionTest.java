package asd.project.key.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import asd.project.key.GameKeyListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for DefaultAction")
class DefaultActionTest {

  private DefaultAction sut;
  private GameKeyListener gameKeyListenerMock;

  @BeforeEach
  void beforeEach() {
    sut = new DefaultAction();
    gameKeyListenerMock = mock(GameKeyListener.class);
  }

  @Test
  @DisplayName("perform1_tests whether character gets appended when allowed")
  void perform1() {
    // Arrange
    String characters = "ho";
    char allowedCharacter = 'i';

    // Act
    String result = sut.perform(gameKeyListenerMock, characters, allowedCharacter);

    // Assert
    assertEquals(characters + allowedCharacter, result);
  }

  @Test
  @DisplayName("perform2_tests whether character doesnt get appended when not allowed")
  void perform2() {
    // Arrange
    String characters = "hoi";
    char disallowedCharacter = '$';

    // Act
    String result = sut.perform(gameKeyListenerMock, characters, disallowedCharacter);

    // Assert
    assertEquals(characters, result);
  }
}