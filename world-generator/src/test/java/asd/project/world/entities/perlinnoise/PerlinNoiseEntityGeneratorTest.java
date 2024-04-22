package asd.project.world.entities.perlinnoise;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.config.world.EntityConfig;
import asd.project.domain.Chunk;
import asd.project.domain.Grid;
import asd.project.domain.Position;
import asd.project.domain.tile.FloorTile;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PerlinNoiseEntityGeneratorTest {

  PerlinNoiseEntityGenerator sut;

  EntityConfig entityConfigMock;

  @BeforeEach
  void beforeEach() {
    sut = new PerlinNoiseEntityGenerator();
    entityConfigMock = new EntityConfig(10, 50, 0);
  }

  @Test
  void generateItemsCallsPlaceItem() {

    //arrange
    int size = 400;
    Grid grid = new Grid(size, size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        grid.setTile(new FloorTile(new Position(i, j)));
      }
    }
    Chunk chunk = Mockito.mock(Chunk.class);

    when(chunk.getRandom()).thenReturn(new Random());
    when(chunk.getWidth()).thenReturn(size);
    when(chunk.getHeight()).thenReturn(size);
    when(chunk.getGrid()).thenReturn(grid);

    //act
    sut.generateMaps(chunk);
    sut.generateItems(chunk, new EntityConfig(0, 0, 0));

    //assert
    verify(chunk, times(80)).addEntity(any());

  }
}