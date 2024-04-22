package asd.project.config.world;

import java.util.UUID;

public final class BSPChunkGenerationConfiguration extends ChunkGenerationConfiguration {

  private final int width;
  private final int height;

  public BSPChunkGenerationConfiguration(int chunkSeed, UUID chunkId,
      EntityConfig entityConfig, RoomGenerationConfiguration roomConfiguration,
      int width, int height) {
    super(chunkSeed, chunkId, entityConfig, roomConfiguration);
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

}
