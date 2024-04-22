package asd.project.domain.tile;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Position;
import asd.project.domain.entity.Entity;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for tiles which are part of a tile matrix. Subclasses must extend this class to define
 * specific types of tiles.
 */
public abstract class Tile implements Cloneable {

  private final CharacterConfiguration character;
  private final boolean walkable;
  private Position position;
  private Entity entity;

  /**
   * Constructs a Tile with the specified character configuration and position.
   *
   * @param character The character configuration of the tile.
   * @param position  The position of the tile in the chunk.
   */

  protected Tile(CharacterConfiguration character, Position position, boolean walkable) {
    this.character = character;
    this.position = position;
    this.walkable = walkable;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Optional<Entity> getEntity() {
    return Optional.ofNullable(entity);
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  public CharacterConfiguration getCharacter() {
    return character;
  }

  public boolean isWalkable() {
    return !walkable;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tile tile = (Tile) o;

    if (!getEntity().isEmpty()) {
      if (tile.getEntity().isEmpty()) {
        return false;
      } else {
        return getEntity().equals(tile.getEntity());

      }
    }

    return walkable == tile.walkable && character == tile.character && position.equals(
        tile.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(walkable, character, position);
  }
}
