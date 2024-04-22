package asd.project.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for Position")
class PositionTest {

  Position sut;

  @BeforeEach
  void beforeEach() {
    sut = new Position(1, 1);
  }

  @Test
  @DisplayName("add1_WhenPositionIsAdded_ThenPositionIsAdded")
  void add1() {
    // Arrange
    var other = new Position(2, 2);

    // Act
    var result = sut.add(other);

    // Assert
    assertEquals(3, result.getX());
    assertEquals(3, result.getY());
  }

  @Test
  @DisplayName("subtract2_WhenPositionIsSubtracted_ThenPositionIsSubtracted")
  void subtract2() {
    // Arrange
    var other = new Position(2, 2);

    // Act
    var result = sut.subtract(other);

    // Assert
    assertEquals(-1, result.getX());
    assertEquals(-1, result.getY());
  }

  @Test
  @DisplayName("setY3_WhenYIsSet_ThenPositionsAreEqual")
  void setY3() {
    // Arrange

    // Act
    sut.setY(2);

    // Assert
    assertEquals(2, sut.getY());
  }

  @Test
  @DisplayName("setX4_WhenXIsSet_ThenPositionsAreEqual")
  void setX4() {
    // Arrange

    // Act
    sut.setX(2);

    // Assert
    assertEquals(2, sut.getX());
  }
}