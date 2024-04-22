package asd.project.world.connections;

import asd.project.domain.Chunk;

/**
 * An interface for connecting two points together for generating corridors. The implementation
 * should modify the provided chunk by adding the corridors.
 */
public interface ICorridorConnector {

  /**
   * Connects the two positions together via corridor in the provided chunk.
   *
   * @param chunk      The chunk to modify.
   * @param connection The connection to connect.
   */
  void connectPositions(Chunk chunk, PositionConnection connection);
}
