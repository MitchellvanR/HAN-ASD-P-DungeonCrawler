package asd.project.domain.tile;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Position;

public class FloorTile extends Tile {

  public FloorTile(Position position) {
    super(CharacterConfiguration.OPEN_TILE, position, true);
  }
}
