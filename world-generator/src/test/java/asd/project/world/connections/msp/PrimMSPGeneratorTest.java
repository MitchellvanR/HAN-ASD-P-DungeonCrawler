package asd.project.world.connections.msp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import asd.project.domain.Position;
import asd.project.world.rooms.Room;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for PrimMSPGenerator")
class PrimMSPGeneratorTest {

  PrimMSPGenerator sut;

  @BeforeEach
  void beforeEach() {
    sut = new PrimMSPGenerator();
  }

  @Test
  @DisplayName("generateConnections1_ensureTwoNodesCreatesOneConnection")
  void generateConnections1() {
    // Arrange
    var rooms = new ArrayList<Room>();
    rooms.add(new Room(6, 6));
    rooms.add(new Room(6, 6));

    // Act
    var result = sut.generateConnections(rooms);

    // Assert
    assertEquals(1, result.size());
    assertEquals(new Position(3, 3), result.get(0).fromPosition());
  }

  @Test
  @DisplayName("generateConnections2_ensureThreeNodesArentACircle")
  void generateConnections2() {
    // Arrange
    var rooms = new ArrayList<Room>();
    rooms.add(new Room(6, 6));
    rooms.add(new Room(6, 6));
    rooms.add(new Room(6, 6));

    // Act
    var result = sut.generateConnections(rooms);

    // Assert
    assertEquals(2, result.size());
  }
}