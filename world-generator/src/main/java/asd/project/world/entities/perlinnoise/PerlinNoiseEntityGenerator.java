package asd.project.world.entities.perlinnoise;

import asd.project.config.world.EntityConfig;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.Entity;
import asd.project.domain.entity.attribute.DoubleCoins;
import asd.project.domain.entity.attribute.Flag;
import asd.project.domain.entity.attribute.LifeElixer;
import asd.project.domain.entity.attribute.Saw;
import asd.project.domain.entity.monster.DemonEye;
import asd.project.domain.entity.monster.DemonKnight;
import asd.project.domain.entity.monster.Dragon;
import asd.project.domain.entity.monster.HellHound;
import asd.project.domain.entity.monster.Hound;
import asd.project.domain.entity.monster.Lich;
import asd.project.domain.entity.monster.Skeleton;
import asd.project.domain.entity.monster.SkeletonWarrior;
import asd.project.domain.entity.monster.Wolf;
import asd.project.domain.entity.trap.Hole;
import asd.project.domain.tile.FloorTile;
import asd.project.domain.tile.Tile;
import asd.project.world.entities.IEntityGenerator;
import asd.project.world.exceptions.CouldNotGenerateItemsExceptions;
import java.util.Random;
import java.util.UUID;

public class PerlinNoiseEntityGenerator implements IEntityGenerator {

  private final float roughnessLevelMap;
  private final float roughnessLifeMap;
  private Mapping levelMap;
  private Mapping lifeMap;

  public PerlinNoiseEntityGenerator() {
    this.roughnessLevelMap = 0.8f;
    this.roughnessLifeMap = 0.8f;
  }

  @Override
  public void generateEntities(Chunk chunk, EntityConfig entityConfig) {
    generateMaps(chunk);
    generateMobs(chunk, entityConfig);
    generateItems(chunk, entityConfig);
  }

  public void generateMaps(Chunk chunk) {
    levelMap = new Mapping(chunk, roughnessLevelMap);
    lifeMap = new Mapping(chunk, roughnessLifeMap);

    levelMap.generateMap();
    lifeMap.generateMap();
  }

  public void generateMobs(Chunk chunk, EntityConfig entityConfig) {
    var random = chunk.getRandom();
    int mobCount = random.nextInt(entityConfig.getMinMonsterCount(),
        entityConfig.getMaxMonsterCount());
    for (int i = 0; i < mobCount; i++) {
      Tile tile = selectRandomTile(chunk, random);
      placeMobOnTile(chunk, tile, entityConfig);
    }
  }

  private void placeMobOnTile(Chunk chunk, Tile tile, EntityConfig entityConfig) {
    Entity[][] mobSpawn = new Entity[3][3];

    int power = entityConfig.getPower();
    int health = entityConfig.getHealth();

// Skeleton
    mobSpawn[0][0] = new Skeleton(
        chunk,
        tile.getPosition(),
        new UUID(1, 200),
        power,
        health,
        20
    );

// SkeletonWarrior
    mobSpawn[0][1] = new SkeletonWarrior(
        chunk,
        tile.getPosition(),
        new UUID(2, 201),
        power,
        health,
        20
    );

// Lich
    mobSpawn[0][2] = new Lich(
        chunk,
        tile.getPosition(),
        new UUID(3, 202),
        power,
        health,
        20
    );

// Wolf
    mobSpawn[1][0] = new Wolf(
        chunk,
        tile.getPosition(),
        new UUID(4, 203),
        power,
        health,
        20
    );

// Hound
    mobSpawn[1][1] = new Hound(
        chunk,
        tile.getPosition(),
        new UUID(5, 204),
        power,
        health,
        20
    );

// HellHound
    mobSpawn[1][2] = new HellHound(
        chunk,
        tile.getPosition(),
        new UUID(6, 205),
        power,
        health,
        20
    );

// DemonEye
    mobSpawn[2][0] = new DemonEye(
        chunk,
        tile.getPosition(),
        new UUID(7, 206),
        power,
        health,
        20
    );

// DemonKnight
    mobSpawn[2][1] = new DemonKnight(
        chunk,
        tile.getPosition(),
        new UUID(8, 207),
        power,
        health,
        20
    );

// Dragon
    mobSpawn[2][2] = new Dragon(
        chunk,
        tile.getPosition(),
        new UUID(9, 208),
        power,
        health,
        20
    );

    int y = tile.getPosition().getY();
    int x = tile.getPosition().getX();
    Entity monster = mobSpawn[lifeMap.map[y][x] - 1][levelMap.map[y][x] - 1];
    chunk.addEntity(monster);
  }

  public void generateItems(Chunk chunk, EntityConfig entityConfig) {

    int itemCount = entityConfig.getItemCount();
    int flagCount = entityConfig.getFlagCount();

    Random random = chunk.getRandom();

    int sawsToPlace = itemCount;
    int elixersToPlace = itemCount;
    int coinsToPlace = itemCount;
    int holesToPlace = itemCount;
    int itemsToPlace = sawsToPlace + elixersToPlace + coinsToPlace + holesToPlace;
    int oldItemsToPlace;
    int maxRepeatCounts = 1000;
    int minLevelValue = 1;

    while (flagCount != 0) {
      Tile tileOfChoice = selectRandomTile(chunk, random);
      if (tileOfChoice instanceof FloorTile && tileOfChoice.getEntity().isEmpty()) {
        tileOfChoice.setEntity(new Flag(chunk, tileOfChoice.getPosition()));
        flagCount--;
      }
    }

    do {
      oldItemsToPlace = sawsToPlace + elixersToPlace + coinsToPlace + holesToPlace;
      Tile chosenTile = selectRandomTile(chunk, random);
      if (!(chosenTile instanceof FloorTile && chosenTile.getEntity().isEmpty())) {
        continue;
      }

      int levelValue = levelMap.map[chosenTile.getPosition().getY()][chosenTile.getPosition()
          .getX()];

      if (levelValue > 1) {

        boolean sawOrElixer = random.nextBoolean();

        if (sawOrElixer && sawsToPlace > 0) {
          Entity item = new Saw(chunk, chosenTile.getPosition());
          chunk.addEntity(item);
          sawsToPlace--;
        } else if (elixersToPlace > 0) {
          Entity item = new LifeElixer(chunk, chosenTile.getPosition());
          chunk.addEntity(item);
          elixersToPlace--;
        } else if (holesToPlace > 0) {
          Entity item = new Hole(chunk, chosenTile.getPosition(), entityConfig.getPower());
          chunk.addEntity(item);
          holesToPlace--;
        } else {
          Entity item = new DoubleCoins(chunk, chosenTile.getPosition());
          chunk.addEntity(item);
          coinsToPlace--;
        }
      }
      itemsToPlace = sawsToPlace + elixersToPlace + coinsToPlace + holesToPlace;

      if (oldItemsToPlace == itemsToPlace) {
        maxRepeatCounts--;
        if (maxRepeatCounts == 0) {
          if (minLevelValue == 0) {
            throw new CouldNotGenerateItemsExceptions(
                "Chunk has not the right dimensions to spawn items");
          } else {
            minLevelValue = 0;
            maxRepeatCounts = 100;
          }
        }
      }

    } while (itemsToPlace != 0);
  }

  private Tile selectRandomTile(Chunk chunk, Random random) {
    Tile tile = null;
    while (!checkToPlace(tile)) {
      var position = new Position(
          random.nextInt(0, chunk.getWidth()),
          random.nextInt(0, chunk.getHeight())
      );
      tile = chunk.getGrid().getTile(position);
    }
    return tile;
  }

  private boolean checkToPlace(Tile tile) {
    return tile != null && tile.getEntity().isEmpty() && (tile instanceof FloorTile);
  }
}
