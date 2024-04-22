package asd.project.config.world;

import java.util.UUID;

public class ChunkGenerationConfiguration {

  private final int chunkSeed;
  private final UUID chunkId;
  private final EntityConfig entityConfig;
  private final RoomGenerationConfiguration roomConfiguration;

  public ChunkGenerationConfiguration(
      int chunkSeed,
      UUID chunkId,
      EntityConfig entityConfig,
      RoomGenerationConfiguration roomConfiguration
  ) {
    this.chunkSeed = chunkSeed;
    this.chunkId = chunkId;
    this.entityConfig = entityConfig;
    this.roomConfiguration = roomConfiguration;
  }

  public int getChunkSeed() {
    return chunkSeed;
  }

  public EntityConfig getEntityConfig() {
    return entityConfig;
  }

  public RoomGenerationConfiguration getRoomConfiguration() {
    return roomConfiguration;
  }

  public UUID getChunkId() {
    return chunkId;
  }
}
