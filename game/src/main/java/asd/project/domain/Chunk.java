package asd.project.domain;

import asd.project.domain.entity.Entity;
import asd.project.domain.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Chunk {

  private final UUID uuid;
  private final int width;
  private final int height;
  private final Random random;
  private final List<Entity> entityList;
  private final HashMap<UUID, Player> playerHashMap;
  private World world;
  private Grid grid;

  public Chunk(int width, int height, int seed, UUID uuid) {
    this.width = width;
    this.height = height;
    this.random = new Random(seed);
    this.grid = new Grid(width, height);

    this.uuid = uuid;
    entityList = new ArrayList<>();
    playerHashMap = new HashMap<>();
  }

  public Grid getGrid() {
    return grid;
  }

  public void setGrid(Grid grid) {
    this.grid = grid;
  }

  public UUID getUUID() {
    return uuid;
  }

  public void addEntity(Entity entity) {
    entityList.add(entity);

    Position tilePosition = entity.getPosition();
    var tile = grid.getTile(tilePosition);
    tile.setEntity(entity);
  }

  public void addToPlayerHashMap(Player player) {
    var tile = grid.getTile(player.getPosition());
    tile.setEntity(player);
    playerHashMap.put(player.getUUID(), player);
  }

  public Random getRandom() {
    return random;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Map<UUID, Player> getPlayerHashMap() {
    return playerHashMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Chunk chunk = (Chunk) o;
    return width == chunk.width
        && height == chunk.height
        && Objects.equals(entityList, chunk.entityList)
        && Objects.equals(playerHashMap, chunk.playerHashMap)
        && Objects.equals(grid, chunk.grid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height, entityList, playerHashMap, grid);
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }
}
