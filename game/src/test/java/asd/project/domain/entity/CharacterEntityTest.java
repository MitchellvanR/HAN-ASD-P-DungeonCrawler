package asd.project.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Grid;
import asd.project.domain.Position;
import asd.project.domain.World;
import asd.project.domain.tile.Tile;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for CharacterEntity")
class CharacterEntityTest {

  CharacterEntity sut;

  World world;
  Chunk chunk;
  Position position;

  @BeforeEach
  void beforeEach() {
    chunk = spy(new Chunk(10, 10, 10, UUID.randomUUID()));
    var grid = new Grid(10, 10);
    chunk.setGrid(grid);
    position = new Position(2, 0);

    sut = new CharacterEntityImpl(chunk, position);
  }

  @Test
  @DisplayName("move1_WhenMoveIsCalled_ThenPositionIsUpdated")
  void move1() {
    // Act
    sut.move(new Position(2, 0));

    // Assert
    assertEquals(new Position(2, 0), sut.getPosition());
  }

  @Test
  @DisplayName("publicInteract1_playerAttackEnemy_ThenEnemyTakesDamage")
  void publicInteract1() {
    // Arrange
    var player = mock(Player.class);
    player.setMoney(10);
    var tile = new TileImpl(new Position(3, 0), true);
    tile.setEntity(player);

    var mockedChunk = mock(Chunk.class);
    var mockedWorld = mock(World.class);

    when(player.getChunk()).thenReturn(mockedChunk);
    when(mockedChunk.getWorld()).thenReturn(mockedWorld);
    when(mockedWorld.getPlayer()).thenReturn(player);
    doReturn(UUID.randomUUID()).when(player).getUUID();

    when(player.getPower()).thenReturn(1);
    when(player.getPowerRate()).thenReturn(1);

    sut.getChunk().getGrid().setTile(tile);
    sut.setHealth(2);

    int expectedHealth = sut.health - (player.getPower() * player.getPowerRate());

    // Act
    sut.interact(player);

    // Assert
    assertEquals(expectedHealth, sut.getHealth());
  }

  @Test
  @DisplayName("publicInteract2_playerAttackEnemy_ThenEnemyDies")
  void interact2() {
    // Arrange
    var player = mock(Player.class);
    player.setMoney(10);
    var tile = new TileImpl(new Position(2, 0), true);
    tile.setEntity(player);

    var mockedChunk = mock(Chunk.class);
    var mockedWorld = mock(World.class);

    when(player.getChunk()).thenReturn(mockedChunk);
    when(mockedChunk.getWorld()).thenReturn(mockedWorld);
    when(mockedWorld.getPlayer()).thenReturn(player);
    doReturn(UUID.randomUUID()).when(player).getUUID();

    when(player.getPower()).thenReturn(1);
    when(player.getPowerRate()).thenReturn(1);

    sut.getChunk().getGrid().setTile(tile);
    sut.setHealth(1);

    int expectedHealth = sut.health - (player.getPower() * player.getPowerRate());

    // Act
    sut.interact(player);

    // Assert
    assertTrue(!sut.getChunk().getGrid().getTile(sut.getPosition()).getEntity().isPresent());
    assertEquals(expectedHealth, sut.getHealth());
    assertEquals(0, sut.getMoney());
  }

  class CharacterEntityImpl extends CharacterEntity {

    public CharacterEntityImpl(Chunk chunk, Position position) {
      super(CharacterConfiguration.DRAGON_ENTITY, chunk, position, UUID.randomUUID(),
          EntityConstants.PLAYER_POWER, EntityConstants.PLAYER_MAX_HEALTH, 10);
    }
  }

  class TileImpl extends Tile {

    public TileImpl(Position position, boolean walkable) {
      super(null, position, walkable);
    }
  }
}