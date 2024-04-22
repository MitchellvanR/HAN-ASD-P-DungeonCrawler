package asd.project.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import asd.project.IWorldGenerator;
import asd.project.config.world.BSPChunkGenerationConfiguration;
import asd.project.config.world.ChunkGenerationConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.config.world.RoomGenerationConfiguration;
import asd.project.config.world.WorldGeneratorConfiguration;
import asd.project.domain.Chunk;
import asd.project.world.chunk.bsp.BSPChunkGenerator;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration-test")
@DisplayName("Integration-test-WorldGenerator")
class WorldGeneratorIT {

  IWorldGenerator sut;
  ChunkGenerationConfiguration chunkConfig;
  EntityConfig entityConfig;
  RoomGenerationConfiguration roomConfig;
  WorldGeneratorConfiguration worldGeneratorConfiguration;

  @BeforeEach
  void setup() {

    sut = new BSPChunkGenerator();

    entityConfig = new EntityConfig(10, 50, 0);

    roomConfig = new RoomGenerationConfiguration(
        10,
        10,
        20,
        20
    );

    worldGeneratorConfiguration = new WorldGeneratorConfiguration(72, 240, 10);

    chunkConfig = new BSPChunkGenerationConfiguration(
        10,
        UUID.randomUUID(),
        entityConfig,
        roomConfig,
        21,
        21

    );
    sut.setWorldConfiguration(worldGeneratorConfiguration);
  }

  @Test
  @DisplayName("generateChunk1_sameSeedSameWorld")
  void sameSeedSameWorld() {

    //act
    Chunk chunk0 = sut.generateChunk(chunkConfig);
    Chunk chunk1 = sut.generateChunk(chunkConfig);

    //assert
    assertEquals(chunk1.getGrid(), chunk0.getGrid());
  }

  @Test
  @DisplayName("generateChunk2_differentSeedDifferentWorld")
  void differentSeedDifferentWorld() {
    //arrange
    ChunkGenerationConfiguration chunkConfigWithOtherSeed = new BSPChunkGenerationConfiguration(11,
        UUID.randomUUID(), entityConfig, roomConfig, 1000, 300);
    //act
    Chunk chunk0 = sut.generateChunk(chunkConfig);
    Chunk chunk1 = sut.generateChunk(chunkConfigWithOtherSeed);

    //assert
    assertNotEquals(chunk1.getGrid(), chunk0.getGrid());
  }

}
