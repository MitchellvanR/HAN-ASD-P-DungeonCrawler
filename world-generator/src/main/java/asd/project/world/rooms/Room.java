package asd.project.world.rooms;

import asd.project.domain.Grid;
import asd.project.domain.Position;
import asd.project.domain.tile.Tile;

public class Room {

  private final int height;

  private final int width;

  private final Grid grid;

  public Room(int width, int height) {
    this.height = height;
    this.width = width;
    this.grid = new Grid(width, height);
  }

  public Grid getGrid() {
    return grid;
  }

  public Tile getRelativeCenterTile() {
    var centerOffset = new Position(width / 2, height / 2);
    return this.grid.getTile(centerOffset);
  }
}
