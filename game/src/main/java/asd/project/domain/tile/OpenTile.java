package asd.project.domain.tile;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Position;

public class OpenTile extends Tile {

  public OpenTile(Position position) {
    super(CharacterConfiguration.OPEN_TILE, position, true);
  }
}
