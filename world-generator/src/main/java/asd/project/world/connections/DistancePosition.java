package asd.project.world.connections;

import asd.project.domain.Position;

public class DistancePosition {

  private final int distance;
  private final Position position;

  public DistancePosition(Position fromPosition, Position toPosition) {
    this.position = fromPosition;
    this.distance = fromPosition.distanceTo(toPosition);
  }

  public Position getPosition() {
    return position;
  }

  public int getDistance() {
    return distance;
  }
}
