package asd.project.domain.tile;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Position;

public class EmptyTile extends Tile {

  public EmptyTile(Position position) {
    super(CharacterConfiguration.EMPTY_TILE, position, true);
  }
}
