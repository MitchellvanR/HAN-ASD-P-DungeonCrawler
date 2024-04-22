package asd.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import asd.project.client.Client;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.exceptions.UnknownEventException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for NetworkStructure")
class NetworkStructureTest {

  private NetworkStructure sut;

  @BeforeEach
  void setUp() {
    sut = new Client(mock(Network.class), "0.0.0.0", 0);
  }

  @Test
  @DisplayName("createEvent1_tests whether the object is with EventType NetworkGameState when the input is used with a GameStateDTO.")
  void createEvent1() {
    // Arrange
    EventType expected = EventType.NETWORK_GAME_STATE;
    Object gameState = new GameStateDTO(0, 0, UUID.randomUUID(), UUID.randomUUID(), 0, 0, 0, 0);

    // Act
    Event result = sut.createEvent(gameState);
    EventType actualType = result.eventType();

    // Assert
    assertEquals(actualType, expected);
  }

  @Test
  @DisplayName("createEvent2_tests whether the object is with EventType NetworkMessage when the input is used with a MessageDTO.")
  void createEvent2() {
    // Arrange
    EventType expected = EventType.NETWORK_MESSAGE;
    Object gameState = new MessageDTO("createEvent2_tests");

    // Act
    Event result = sut.createEvent(gameState);
    EventType actualType = result.eventType();

    // Assert
    assertEquals(actualType, expected);
  }

  @Test
  @DisplayName("createEvent3_tests whether the object is with EventType NetworkGameStart when the input is used with a StartGameDTO.")
  void createEvent3() {
    // Arrange
    EventType expected = EventType.NETWORK_GAME_START;
    Object gameState = new StartGameDTO(0, 0, 0, UUID.randomUUID());

    // Act
    Event result = sut.createEvent(gameState);
    EventType actualType = result.eventType();

    // Assert
    assertEquals(actualType, expected);
  }

  @Test()
  @DisplayName("createEvent4_tests whether it returns a UnknownEventException when the object is null and the input is used without a DTO.")
  void createEvent4() {
    // Act & Assert
    assertThrows(UnknownEventException.class, () -> sut.createEvent(mock(Object.class)));
  }
}