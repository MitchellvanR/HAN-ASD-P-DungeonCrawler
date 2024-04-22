package asd.project.world.entities;

import asd.project.config.world.EntityConfig;
import asd.project.domain.Chunk;

/**
 * Interface for entity generators. An entity generator is responsible for generating entities and
 * placing them in a chunk. The entity generator is used by the chunk generator. Implementing
 * classes should override the generateEntities method to define the logic for generating entities.
 */
public interface IEntityGenerator {

  /**
   * Generate entities and place them in the chunk.
   *
   * @param chunk        The chunk to place the entities in.
   * @param entityConfig The configuration for the entities.
   */
  void generateEntities(Chunk chunk, EntityConfig entityConfig);
}
