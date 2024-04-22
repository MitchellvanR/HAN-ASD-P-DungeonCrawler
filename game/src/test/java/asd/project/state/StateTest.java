package asd.project.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import asd.project.Game;
import asd.project.command.Command;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for State")
class StateTest {

  private StartState sut;

  @BeforeEach
  void setUp() {
    sut = new StartState(mock(Game.class));
  }

  @Test
  @DisplayName("getCommandFromList1_tests whether the command is in the command list when the input is used with /host.")
  void getCommandFromList1() {
    // Arrange
    var expected = true;

    // Act
    Optional<Command> command = sut.getCommandFromList("/host");
    var actual = command.isPresent();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("getCommandFromList2_tests whether the command is in the command list when the input is used with /command {parameter}.")
  void getCommandFromList2() {
    // Arrange
    var expected = true;

    // Act
    Optional<Command> command = sut.getCommandFromList("/host test");
    var actual = command.isPresent();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("getCommandFromList3_tests whether the command is not in the command list when the input is used with /help game.")
  void getCommandFromList3() {
    // Arrange
    var expected = false;

    // Act
    Optional<Command> command = sut.getCommandFromList("/help game");
    var actual = command.isPresent();

    // Assert
    assertEquals(expected, actual);
  }
}
