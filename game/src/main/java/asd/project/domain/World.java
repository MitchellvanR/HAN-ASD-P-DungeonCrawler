package asd.project.domain;

import static asd.project.Viewport.VIEWPORT_HEIGHT;
import static asd.project.Viewport.VIEWPORT_WIDTH;

import asd.project.IWorldGenerator;
import asd.project.config.world.BSPChunkGenerationConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.config.world.RoomGenerationConfiguration;
import asd.project.config.world.WorldGeneratorConfiguration;
import asd.project.domain.entity.Player;
import asd.project.domain.tile.FloorTile;
import asd.project.domain.tile.Tile;
import java.util.Random;
import java.util.UUID;

public class World {

  private static final int CHUNK_WIDTH = 2 * VIEWPORT_WIDTH;
  private static final int CHUNK_HEIGHT = 3 * VIEWPORT_HEIGHT;
  private final IWorldGenerator worldGenerator;
  private Player player;
  private Random random;

  public World(IWorldGenerator worldGenerator) {
    this.worldGenerator = worldGenerator;
  }

  public void initializeWorld(int worldSeed) {
    this.random = new Random(worldSeed);
    var worldConfiguration = new WorldGeneratorConfiguration(CHUNK_WIDTH, CHUNK_HEIGHT, worldSeed);
    worldGenerator.setWorldConfiguration(worldConfiguration);
    this.random = new Random(worldConfiguration.worldSeed());
  }

  public Player initializePlayer(Chunk chunk) {
    Tile tile;

    do {
      var grid = chunk.getGrid();

      tile = chunk.getGrid().getTile(
          new Position(
              random.nextInt(0, grid.getWidth()),
              random.nextInt(0, grid.getHeight())
          )
      );
    }
    while (tile == null || tile.isWalkable() || tile.getEntity().isPresent()
        || !(tile instanceof FloorTile));

    var newplayer = new Player(chunk);
    newplayer.move(tile.getPosition());
    chunk.addToPlayerHashMap(newplayer);

    this.player = newplayer;
    return newplayer;
  }

  public Player getPlayer() {
    return player;
  }

  public Chunk expand(UUID uuid) {
    EntityConfig entityConfig = new EntityConfig();
    entityConfig = entityConfig.readEntityConfig();
    var chunkConfiguration = new BSPChunkGenerationConfiguration(
        random.nextInt(),
        uuid,
        entityConfig,
        new RoomGenerationConfiguration(
            10,
            10,
            20,
            20
        ),
        21,
        21

    );

    var chunk = worldGenerator.generateChunk(
        chunkConfiguration
    );
    chunk.setWorld(this);

    return chunk;
  }

  public int getChunkWidth() {
    return CHUNK_WIDTH;
  }

  public int getChunkHeight() {
    return CHUNK_HEIGHT;
  }
}
