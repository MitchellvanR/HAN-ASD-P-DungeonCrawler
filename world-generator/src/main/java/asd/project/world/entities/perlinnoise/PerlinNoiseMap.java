package asd.project.world.entities.perlinnoise;

import java.util.Random;

public class PerlinNoiseMap {

  public final float[][] perlinNoiseMatrix;
  private final Random rd;
  private final float roughness;
  private final float noiseValue;

  public PerlinNoiseMap(float roughness, int width, int height, Random rd) {
    this.roughness = roughness / width;
    this.perlinNoiseMatrix = new float[height][width];
    this.rd = rd;
    this.noiseValue = 0.5f;
  }

  public void initialise() {
    int xSize = perlinNoiseMatrix.length - 1;
    int ySize = perlinNoiseMatrix[0].length - 1;

    this.perlinNoiseMatrix[0][0] = this.rd.nextFloat() - noiseValue;
    this.perlinNoiseMatrix[0][ySize] = this.rd.nextFloat() - noiseValue;
    this.perlinNoiseMatrix[xSize][0] = this.rd.nextFloat() - noiseValue;
    this.perlinNoiseMatrix[xSize][ySize] = this.rd.nextFloat() - noiseValue;

    generate(0, 0, xSize, ySize);
  }

  private float randomPoints(float v, int l, int h) {
    return v + this.roughness * (float) (this.rd.nextGaussian(5, 1) * (h - l));
  }

  private void generate(int xl, int yl, int xh, int yh) {
    int xm = (xl + xh) / 2;
    int ym = (yl + yh) / 2;
    if ((xl == xm) && (yl == ym)) {
      return;
    }

    this.perlinNoiseMatrix[xm][yl] =
        noiseValue * (this.perlinNoiseMatrix[xl][yl] + this.perlinNoiseMatrix[xh][yl]);
    this.perlinNoiseMatrix[xm][yh] =
        noiseValue * (this.perlinNoiseMatrix[xl][yh] + this.perlinNoiseMatrix[xh][yh]);
    this.perlinNoiseMatrix[xl][ym] =
        noiseValue * (this.perlinNoiseMatrix[xl][yl] + this.perlinNoiseMatrix[xl][yh]);
    this.perlinNoiseMatrix[xh][ym] =
        noiseValue * (this.perlinNoiseMatrix[xh][yl] + this.perlinNoiseMatrix[xh][yh]);

    float v = randomPoints(
        noiseValue * (this.perlinNoiseMatrix[xm][yl] + this.perlinNoiseMatrix[xm][yh]), xl + yl,
        yh + xh);
    this.perlinNoiseMatrix[xm][ym] = v;
    this.perlinNoiseMatrix[xm][yl] = randomPoints(this.perlinNoiseMatrix[xm][yl], xl, xh);
    this.perlinNoiseMatrix[xm][yh] = randomPoints(this.perlinNoiseMatrix[xm][yh], xl, xh);
    this.perlinNoiseMatrix[xl][ym] = randomPoints(this.perlinNoiseMatrix[xl][ym], yl, yh);
    this.perlinNoiseMatrix[xh][ym] = randomPoints(this.perlinNoiseMatrix[xh][ym], yl, yh);

    generate(xl, yl, xm, ym);
    generate(xm, yl, xh, ym);
    generate(xl, ym, xm, yh);
    generate(xm, ym, xh, yh);
  }
}
