package asd.project.domain.tile;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Position;

public class WallTile extends Tile {

  public WallTile(Position position) {
    super(CharacterConfiguration.WALL_TILE, position, false);
  }
}

