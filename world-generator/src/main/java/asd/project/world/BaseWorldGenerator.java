package asd.project.world;

import asd.project.IWorldGenerator;
import asd.project.config.world.ChunkGenerationConfiguration;
import asd.project.config.world.WorldGeneratorConfiguration;
import asd.project.domain.Chunk;
import asd.project.world.entities.IEntityGenerator;
import asd.project.world.entities.perlinnoise.PerlinNoiseEntityGenerator;

/**
 * Base class for world generators. It provides a default implementation for combining the world and
 * chunk configurations.
 */
public abstract class BaseWorldGenerator implements IWorldGenerator {

  protected WorldGeneratorConfiguration worldConfiguration;
  private IEntityGenerator entityGenerator;

  protected BaseWorldGenerator() {
    this.entityGenerator = new PerlinNoiseEntityGenerator();
  }

  public Chunk generateChunk(ChunkGenerationConfiguration chunkConfiguration) {
    if (worldConfiguration == null) {
      throw new IllegalStateException("World configuration must be set.");
    }

    var configuration = createConfiguration(chunkConfiguration);

    if (!isConfigurationValid(configuration)) {
      throw new IllegalArgumentException("The configuration is not valid.");
    }

    var chunk = internalGenerateChunk(configuration);
    entityGenerator.generateEntities(chunk, chunkConfiguration.getEntityConfig());

    return chunk;
  }

  private WorldGenerationConfiguration createConfiguration(
      ChunkGenerationConfiguration chunkConfiguration) {
    return new WorldGenerationConfiguration(
        worldConfiguration,
        chunkConfiguration
    );
  }

  /**
   * After the configuration is created. This method is called to generate the chunk on the
   * implementing class.
   *
   * @param configuration The world generation configuration containing the world and chunk
   *                      configurations.
   * @return The generated chunk.
   */
  protected abstract Chunk internalGenerateChunk(WorldGenerationConfiguration configuration);

  protected abstract boolean isConfigurationValid(WorldGenerationConfiguration configuration);

  public void setEntityGenerator(IEntityGenerator entityGenerator) {
    this.entityGenerator = entityGenerator;
  }

  public WorldGeneratorConfiguration getWorldConfiguration() {
    return worldConfiguration;
  }

  @Override
  public void setWorldConfiguration(WorldGeneratorConfiguration worldConfiguration) {
    if (this.worldConfiguration == null) {
      this.worldConfiguration = worldConfiguration;
    } else {
      throw new IllegalStateException("World configuration is already set.");
    }
  }
}
