package asd.project.world.rooms;

import asd.project.config.world.RoomGenerationConfiguration;
import java.util.Random;

/**
 * Interface for room generators. A room generator is responsible for generating rooms. The room
 * generator is used by the chunk generator. Implementing classes should override the generateRoom
 * method to define the logic for generating rooms.
 */
public interface IRoomGenerator {

  /**
   * Generate a room.
   *
   * @param configuration The configuration for the room.
   * @param random        The random number generator.
   * @return The generated room.
   */
  Room generateRoom(RoomGenerationConfiguration configuration, Random random);
}
