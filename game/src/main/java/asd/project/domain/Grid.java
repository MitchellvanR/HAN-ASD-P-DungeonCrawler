package asd.project.domain;

import asd.project.domain.tile.EmptyTile;
import asd.project.domain.tile.Tile;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Grid {

  private static final Logger LOGGER = Logger.getLogger(Grid.class.getName());
  private final Tile[][] tiles;
  private final int width;
  private final int height;

  public Grid(int width, int height) {
    this.width = width;
    this.height = height;

    tiles = new Tile[height][width];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        tiles[y][x] = new EmptyTile(new Position(x, y));
      }
    }
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public Tile getTile(Position position) {
    return tiles[position.getY()][position.getX()];
  }

  public void setTile(Tile tile) {
    var position = tile.getPosition();

    tiles[position.getY()][position.getX()] = tile;
  }

  public void placeGrid(Position position, Grid otherGrid) {
    if (!areaFits(position, otherGrid)) {
      throw new IllegalArgumentException("Area does not fit in grid");
    }

    for (int x = 0; x < otherGrid.getWidth(); x++) {
      for (int y = 0; y < otherGrid.getHeight(); y++) {
        var otherTile = otherGrid.getTile(new Position(x, y));

        otherTile.setPosition(new Position(position.getY() + y, position.getX() + x));
        setTile(otherTile);
      }
    }
  }

  public Grid takeArea(Position position, int width, int height) {
    if (!areaFits(position, width, height)) {
      throw new IllegalArgumentException("Area does not fit in grid");
    }

    return takeAreaUnsafe(position, width, height);
  }

  public Grid takeAreaUnsafe(Position position, int width, int height) {
    var grid = new Grid(width, height);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        var tile = getTile(new Position(position.getX() + x, position.getY() + y));
        try {
          Tile clonedTile = (Tile) tile.clone();
          clonedTile.setPosition(new Position(x, y));
          grid.setTile(clonedTile);
        } catch (CloneNotSupportedException exception) {
          LOGGER.log(Level.WARNING, String.format("Encountered error: %s.", exception));
        }
      }
    }

    return grid;
  }

  public boolean areaFits(Position position, int width, int height) {
    return position.getX() + height <= this.height
        && position.getY() + width <= this.width
        && position.getX() >= 0
        && position.getY() >= 0;
  }

  public boolean areaFits(Position position, Grid grid) {
    return areaFits(position, grid.getWidth(), grid.getHeight());
  }

  public Grid copy() throws CloneNotSupportedException {
    var copy = new Grid(width, height);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        var tile = getTile(new Position(x, y));
        copy.setTile((Tile) tile.clone());
      }
    }

    return copy;
  }

  public boolean isPositionInGrid(Position position) {
    return position.getX() >= 0 && position.getX() < width && position.getY() >= 0
        && position.getY() < height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Grid grid = (Grid) o;

    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        Tile tile1 = tiles[i][j];
        Tile tile2 = grid.tiles[i][j];
        if (!tile1.equals(tile2)) {
          return false;
        }

      }
    }

    return width == grid.width && height == grid.height;
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height);
  }

}


