package asd.project.domain.entity;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a base class for an entity. An entity has a unique identifier (UUID), character
 * configuration, current chunk, and a position in the chunk. <br/><br/> Subclasses must extend this
 * class to define specific types of entities.
 */
public abstract class Entity {

  private final UUID uuid;
  private final CharacterConfiguration character;
  private final Chunk chunk;
  private Position position;

  /**
   * Constructs an Entity with the specified character configuration, current chunk, position and
   * UUID.
   *
   * @param character The character configuration of the tile.
   * @param chunk     The chunk on which the entity currently stands.
   * @param position  The position of the entity in the chunk.
   * @param uuid      The unique ID of the entity.
   */
  protected Entity(CharacterConfiguration character, Chunk chunk, Position position, UUID uuid) {
    this.uuid = uuid;
    this.character = character;
    this.chunk = chunk;
    this.position = position;
  }

  protected Entity(CharacterConfiguration character, Chunk chunk, Position position) {
    this(character, chunk, position, UUID.randomUUID());
  }

  public UUID getUUID() {
    return uuid;
  }

  public Chunk getChunk() {
    return chunk;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public CharacterConfiguration getCharacter() {
    return character;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity entity = (Entity) o;
    return character == entity.character && position.equals(entity.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(character, position);
  }
}
