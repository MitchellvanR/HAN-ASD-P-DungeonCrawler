package asd.project.world.chunk.bsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import asd.project.config.world.BSPChunkGenerationConfiguration;
import asd.project.config.world.ChunkGenerationConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.config.world.RoomGenerationConfiguration;
import asd.project.config.world.WorldGeneratorConfiguration;
import asd.project.world.WorldGenerationConfiguration;
import asd.project.world.entities.IEntityGenerator;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("Tests for BSPChunkGenerator")
class BSPChunkGeneratorTest {

  BSPChunkGenerator sut;

  EntityConfig entityConfigMock;

  @BeforeEach
  void beforeEach() {
    var configuration = new WorldGeneratorConfiguration(80, 24, 10);
    entityConfigMock = new EntityConfig(10, 50, 0);

    var entityGenerator = Mockito.mock(IEntityGenerator.class);
    Mockito.doNothing().when(entityGenerator).generateEntities(Mockito.any(), Mockito.any());

    sut = new BSPChunkGenerator();
    sut.setEntityGenerator(entityGenerator);
    sut.setWorldConfiguration(configuration);
  }

  @Test
  @DisplayName("internalGenerateChunk1_WhenChunkIsGenerated_ThenChunkSizeIsCorrect")
  void internalGenerateChunk1() {
    // Arrange
    var chunkConfiguration = new BSPChunkGenerationConfiguration(
        10,
        null,
        entityConfigMock,
        new RoomGenerationConfiguration(4, 10, 6, 15),
        10,
        25
    );

    // Act
    var chunk = sut.generateChunk(chunkConfiguration);

    // Assert
    assertEquals(80, chunk.getWidth());
    assertEquals(24, chunk.getHeight());
  }

  @Test
  @DisplayName("internalGenerateChunk2_WhenChunkIsGeneratedUsingWrongConfiguration_ThenAnExceptionIsThrown")
  void internalGenerateChunk2() {
    // Act & Assert
    assertThrowsExactly(
        IllegalArgumentException.class,
        () -> sut.generateChunk(new ChunkGenerationConfiguration(10, UUID.randomUUID(), null, null))
    );
  }

  @Test
  @DisplayName("isConfigurationValid3_WhenConfigurationIsValid_ThenTrueIsReturned")
  void isConfigurationValid3() {
    //arrange
    WorldGenerationConfiguration emptyConfiguration = new WorldGenerationConfiguration(null, null);

    WorldGeneratorConfiguration working = new WorldGeneratorConfiguration(100, 100, 1);
    RoomGenerationConfiguration roomZeroValues = new RoomGenerationConfiguration(-1, 0, 0, 0);
    BSPChunkGenerationConfiguration workingChunk = new BSPChunkGenerationConfiguration(1,
        new UUID('s', 's'), entityConfigMock, roomZeroValues, 100, 100);
    WorldGenerationConfiguration filledWithRoomZeroValues = new WorldGenerationConfiguration(
        working, workingChunk);

    RoomGenerationConfiguration bigRoom = new RoomGenerationConfiguration(1000, 1000, 1500, 1500);
    BSPChunkGenerationConfiguration lowChunk = new BSPChunkGenerationConfiguration(1,
        new UUID('s', 's'), entityConfigMock, bigRoom, 100, 100);
    WorldGenerationConfiguration roomBiggerThanChunk = new WorldGenerationConfiguration(working,
        lowChunk);

    WorldGeneratorConfiguration normalWorldGenerator = new WorldGeneratorConfiguration(100, 100, 1);
    RoomGenerationConfiguration normalRoom = new RoomGenerationConfiguration(10, 10, 20, 20);
    BSPChunkGenerationConfiguration normalChunkConfig = new BSPChunkGenerationConfiguration(1,
        new UUID('s', 's'), entityConfigMock, normalRoom, 100, 100);
    WorldGenerationConfiguration normalConfig = new WorldGenerationConfiguration(
        normalWorldGenerator, normalChunkConfig);

    // act // assert
    assertFalse(sut.isConfigurationValid(emptyConfiguration));
    assertFalse(sut.isConfigurationValid(filledWithRoomZeroValues));
    assertFalse(sut.isConfigurationValid(roomBiggerThanChunk));
    assertTrue(sut.isConfigurationValid(normalConfig));
  }
}