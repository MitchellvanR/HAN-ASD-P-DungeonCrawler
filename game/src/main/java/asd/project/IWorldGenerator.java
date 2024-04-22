package asd.project;

import asd.project.config.world.ChunkGenerationConfiguration;
import asd.project.config.world.WorldGeneratorConfiguration;
import asd.project.domain.Chunk;

/**
 * This interface represents the world generator. The implementation of this interface must be able
 * to generate a chunk.
 */
public interface IWorldGenerator {

  /**
   * This method generates a chunk. The chunk is generated according to the configuration passed as
   * a parameter. The configuration contains the information necessary to generate the chunk.
   *
   * @param chunkGenerationConfiguration The configuration used to generate the chunk.
   * @return The generated chunk.
   */
  Chunk generateChunk(ChunkGenerationConfiguration chunkGenerationConfiguration);

  /**
   * This method sets the world configuration. The world configuration is used to generate the
   * chunk.
   *
   * @param worldConfiguration The world configuration.
   * @throws IllegalStateException If the world configuration is already set.
   */
  void setWorldConfiguration(WorldGeneratorConfiguration worldConfiguration);
}
