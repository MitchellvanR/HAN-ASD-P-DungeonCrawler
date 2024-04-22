package asd.project.world.rooms;

import asd.project.config.world.RoomGenerationConfiguration;
import asd.project.domain.Grid;
import asd.project.domain.Position;
import asd.project.domain.tile.FloorTile;
import asd.project.domain.tile.WallTile;
import java.util.Random;

public class SimpleRoomGenerator implements IRoomGenerator {

  @Override
  public Room generateRoom(RoomGenerationConfiguration configuration, Random random) {
    int height = random.nextInt(configuration.minRoomWidth(), configuration.maxRoomWidth());
    int width = random.nextInt(configuration.minRoomHeight(), configuration.maxRoomHeight());

    Room room = new Room(width, height);
    Grid grid = room.getGrid();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        var position = new Position(x, y);
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
          grid.setTile(new WallTile(position));
        } else {
          grid.setTile(new FloorTile(position));
        }
      }
    }
    return room;
  }
}
