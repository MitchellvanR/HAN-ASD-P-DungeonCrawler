package asd.project.key.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import asd.project.key.GameKeyListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for BackAction")
class BackActionTest {

  private BackAction sut;
  private GameKeyListener gameKeyListenerMock;

  @BeforeEach
  void beforeEach() {
    sut = new BackAction();
    gameKeyListenerMock = mock(GameKeyListener.class);
  }

  @Test
  @DisplayName("perform1_tests whether last character gets removed when back space constant gets used")
  void perform1() {
    // Arrange
    String characters = "hello";
    String expected = "hell";

    // Act
    String result = sut.perform(gameKeyListenerMock, characters, ' ');

    // Assert
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("perform2_tests whether empty string gets returned when back space gets called on empty string")
  void perform2() {
    // Arrange
    String characters = "";
    String expected = "";

    // Act
    String result = sut.perform(gameKeyListenerMock, characters, ' ');

    // Assert
    assertEquals(expected, result);
  }


}