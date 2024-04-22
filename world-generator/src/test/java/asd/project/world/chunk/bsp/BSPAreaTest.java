package asd.project.world.chunk.bsp;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import asd.project.config.world.BSPChunkGenerationConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.config.world.RoomGenerationConfiguration;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for BSPArea")
class BSPAreaTest {

  BSPArea sut;

  EntityConfig entityConfigMock;

  @BeforeEach
  void beforeEach() {
    var random = new Random(10);
    entityConfigMock = new EntityConfig(10, 50, 0);
    var configuration = new BSPChunkGenerationConfiguration(
        10,
        UUID.randomUUID(),
        entityConfigMock,
        new RoomGenerationConfiguration(
            1,
            10,
            1,
            10
        ),
        10,
        20
    );

    sut = new BSPArea(0, 0, 1000, 1000, random, configuration);
  }

  @Test
  @DisplayName("splitArea1_WhenAreaIsSplit_ThenLeftAndRightAreasAreNotNull")
  void splitArea1() {
    // Act
    sut.splitArea();

    // Assert
    assertNotNull(sut.getLeftAreaNode().getLeftAreaNode());
    assertNotNull(sut.getLeftAreaNode().getRightAreaNode());
    assertNotNull(sut.getRightAreaNode().getLeftAreaNode());
    assertNotNull(sut.getRightAreaNode().getRightAreaNode());
  }
}