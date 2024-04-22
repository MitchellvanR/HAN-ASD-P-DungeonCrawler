package asd.project.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import asd.project.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for Command")
class CommandTest {

  private StartGameCommand sut;

  @BeforeEach
  void beforeEach() {
    var gameMock = mock(Game.class);
    sut = new StartGameCommand(gameMock);
  }

  @Test
  @DisplayName("getArgument1_check if no argument is returned when command equals command name")
  void getArgument1() {
    // Arrange
    var expected = "";

    // Act
    var actual = sut.getArgument("/start game");

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("getArgument2_check if argument is returned when command has an argument")
  void getArgument2() {
    // Arrange
    var expected = "dit is een argument";

    // Act
    var actual = sut.getArgument("/start game dit is een argument");

    // Assert
    assertEquals(expected, actual);
  }
}
