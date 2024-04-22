package asd.project.world.connections;

import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.tile.EmptyTile;
import asd.project.domain.tile.OpenTile;
import asd.project.domain.tile.Tile;
import asd.project.domain.tile.WallTile;
import java.util.ArrayList;

public class DijkstraCorridorConnector implements ICorridorConnector {

  @Override
  public void connectPositions(Chunk chunk, PositionConnection connection) {
    var fromPosition = connection.fromPosition();
    var toPosition = connection.toPosition();
    var surroundingDistancePositionList = giveSurroundingPositions(fromPosition, toPosition);

    placeCorridor(chunk, fromPosition, surroundingDistancePositionList);

    var nextTilePosition = calculateNextTilePosition(surroundingDistancePositionList);

    if (nextTilePosition != null && !nextTilePosition.equals(toPosition)) {
      connectPositions(chunk, new PositionConnection(nextTilePosition, toPosition));
    }
  }

  private void placeCorridor(Chunk chunk, Position fromPosition,
      ArrayList<DistancePosition> surroundingDistancePositions) {
    Tile tileToReplace = chunk.getGrid().getTile(fromPosition);
    if (tileToReplace instanceof EmptyTile || tileToReplace instanceof WallTile) {
      chunk.getGrid().setTile(new OpenTile(fromPosition));

      for (DistancePosition distancePosition : surroundingDistancePositions) {
        if (chunk.getGrid().getTile(distancePosition.getPosition()) instanceof EmptyTile) {
          chunk.getGrid().setTile(new WallTile(distancePosition.getPosition()));
        }
      }
    }
  }

  private Position calculateNextTilePosition(ArrayList<DistancePosition> nextTileOptions) {
    DistancePosition closestOption = null;
    for (DistancePosition tileOption : nextTileOptions) {
      if (closestOption == null || closestOption.getDistance() > tileOption.getDistance()) {
        closestOption = tileOption;
      }
    }

    return closestOption != null ? closestOption.getPosition() : null;
  }

  private ArrayList<DistancePosition> giveSurroundingPositions(Position fromPosition,
      Position toPosition) {
    var nextTileOptions = new ArrayList<DistancePosition>();

    var left = new Position(fromPosition.getX() - 1, fromPosition.getY());
    var right = new Position(fromPosition.getX() + 1, fromPosition.getY());
    var above = new Position(fromPosition.getX(), fromPosition.getY() - 1);
    var below = new Position(fromPosition.getX(), fromPosition.getY() + 1);

    nextTileOptions.add(new DistancePosition(left, toPosition));
    nextTileOptions.add(new DistancePosition(right, toPosition));
    nextTileOptions.add(new DistancePosition(above, toPosition));
    nextTileOptions.add(new DistancePosition(below, toPosition));

    return nextTileOptions;
  }
}

