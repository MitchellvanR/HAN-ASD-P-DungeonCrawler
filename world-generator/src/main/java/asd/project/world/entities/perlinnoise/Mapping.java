package asd.project.world.entities.perlinnoise;

import asd.project.domain.Chunk;
import java.util.Random;

public class Mapping {

  private final PerlinNoiseMap perlinNoiseMap;
  private final Chunk chunk;
  protected int[][] map;

  public Mapping(Chunk chunk, float roughness) {
    Random rd = chunk.getRandom();
    this.chunk = chunk;
    int width = chunk.getWidth();
    int height = chunk.getHeight();
    this.perlinNoiseMap = new PerlinNoiseMap(roughness, width, height, rd);
  }

  public void generateMap() {
    perlinNoiseMap.initialise();
    map = new int[this.chunk.getHeight()][this.chunk.getWidth()];
    for (int x = 0; x < this.chunk.getHeight(); x++) {
      for (int y = 0; y < this.chunk.getWidth(); y++) {
        if (4 > this.perlinNoiseMap.perlinNoiseMatrix[x][y]) {
          map[x][y] = 1;
        }
        if (4 < this.perlinNoiseMap.perlinNoiseMatrix[x][y]
            && 7 > this.perlinNoiseMap.perlinNoiseMatrix[x][y]) {
          map[x][y] = 2;
        }
        if (7 < this.perlinNoiseMap.perlinNoiseMatrix[x][y]) {
          map[x][y] = 3;
        }
      }
    }
  }
}
