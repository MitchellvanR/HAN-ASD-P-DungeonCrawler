package asd.project.domain;

import java.util.Objects;

public class Position {

  private int x;
  private int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position add(Position other) {
    return new Position(x + other.x, y + other.y);
  }

  public Position subtract(Position other) {
    return new Position(x - other.x, y - other.y);
  }

  public int distanceTo(Position other) {
    return (int) Math.sqrt(
        Math.pow(getX() - (double) other.getX(), 2) + Math.pow(getY() - (double) other.getY(), 2));
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  @Override
  public String toString() {
    return "Position{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}