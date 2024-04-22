package asd.project.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import asd.project.domain.tile.WallTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for Grid")
class GridTest {

  Grid sut;

  @BeforeEach
  void beforeEach() {
    sut = new Grid(10, 10);
  }

  @Test
  @DisplayName("setTile1_WhenTileIsSet_ThenTileIsSet")
  void setTile1() {
    // Arrange
    var tile = new WallTile(new Position(0, 0));

    // Act
    sut.setTile(tile);
    var result = sut.getTile(new Position(0, 0));

    // Assert
    assertEquals(tile, result);
  }

  @Test
  @DisplayName("outsideBoundsThrowsException2_WhenTileIsSetOutsideBounds_ThenExceptionIsThrown")
  void outsideBoundsThrowsException2() {
    // Act & Assert
    assertThrows(IndexOutOfBoundsException.class, () -> {
      sut.getTile(new Position(-1, -1));
    });

    assertThrows(IndexOutOfBoundsException.class, () -> {
      var tile = new WallTile(new Position(100, 10));
      sut.setTile(tile);
    });
  }

  @Test
  @DisplayName("placeGrid3_WhenGridIsPlaced_ThenGridIsPlaced")
  void placeGrid3() {
    // Arrange
    var otherGrid = new Grid(5, 5);
    var tile = new WallTile(new Position(1, 1));

    otherGrid.setTile(tile);

    // Act
    sut.placeGrid(new Position(1, 1), otherGrid);

    // Assert
    assertNotEquals(tile, sut.getTile(new Position(1, 1)));
    assertEquals(tile, sut.getTile(new Position(2, 2)));
  }

  @Test
  @DisplayName("takeArea4_WhenAreaIsTaken_ThenAreaIsTaken")
  void takeArea4() {
    // Arrange
    var tile = new WallTile(new Position(0, 0));

    sut.setTile(tile);

    // Act
    var result = sut.takeArea(new Position(0, 0), 1, 1);

    // Assert
    assertEquals(tile, result.getTile(new Position(0, 0)));
  }

  @Test
  @DisplayName("copy5_WhenGridIsCopied_ThenGridIsCopied")
  void copy5() throws CloneNotSupportedException {
    // Arrange
    var tile = new WallTile(new Position(1, 1));

    sut.setTile(tile);

    // Act
    var result = sut.copy();

    // Assert
    assertEquals(tile, result.getTile(new Position(1, 1)));
  }

  @Test
  @DisplayName("areaFits6_SmallArea_WhenAreaFits_ThenAreaFits")
  void areaFits6() {
    // Arrange
    var otherGrid = new Grid(5, 5);

    // Act & Assert
    assertDoesNotThrow(() -> {
      sut.areaFits(new Position(0, 0), otherGrid);
    });
  }

  @Test
  @DisplayName("areaFits7_SmallAreaOnEdge_WhenAreaFits_ThenAreaFits")
  void areaFits7() {
    // Arrange
    var otherGrid = new Grid(5, 5);

    // Act & Assert
    assertDoesNotThrow(() -> {
      sut.areaFits(new Position(5, 0), otherGrid);
    });
  }

  @Test
  @DisplayName("areaFits8_SmallAreaOverEdge_WhenAreaDoesntFit_ThenExceptionIsThrow")
  void areaFits8() {
    // Arrange
    var otherGrid = new Grid(5, 5);

    // Act
    var result = sut.areaFits(new Position(10, 0), otherGrid);

    // Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("areaFits9_SmallAreaOverEdge_WhenAreaDoesntFit_ThenExceptionIsThrow")
  void areaFits9() {
    // Arrange
    var otherGrid = new Grid(5, 5);

    // Act
    var result = sut.areaFits(new Position(0, 10), otherGrid);

    // Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("areaFits10_EqualSizedArea_WhenAreaFits_ThenAreaFits")
  void areaFits10() {
    // Arrange
    var otherGrid = new Grid(10, 10);

    // Act
    var result = sut.areaFits(new Position(0, 0), otherGrid);

    // Assert
    assertTrue(result);
  }

  @Test
  @DisplayName("takeAreaUnsafe11_ThrowsException_WhenAreaDoesntFit")
  void takeAreaUnsafe11() {
    // Act & Assert
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      sut.takeAreaUnsafe(new Position(10, 0), 5, 5);
    });
  }

  @Test
  @DisplayName("takeAreaUnsafe12_ThrowsNoException_WhenAreaDoesFit")
  void takeAreaUnsafe12() {
    // Act & Assert
    assertDoesNotThrow(() -> {
      sut.takeAreaUnsafe(new Position(0, 0), 5, 5);
    });
  }
}