package asd.project.world.chunk.bsp;

import asd.project.config.world.BSPChunkGenerationConfiguration;
import asd.project.domain.Position;
import java.util.List;
import java.util.Random;

public class BSPArea {

  private final int x;

  private final int y;

  private final int height;

  private final int width;
  private final Random rnd;
  private final BSPChunkGenerationConfiguration configuration;
  private boolean horizontal;
  private BSPArea leftAreaNode;
  private BSPArea rightAreaNode;
  private boolean isLeaf;
  private BSPLeaf bspLeaf;

  public BSPArea(int x, int y, int width, int height, Random rnd,
      BSPChunkGenerationConfiguration configuration) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.rnd = rnd;
    this.configuration = configuration;
  }

  void splitArea() {
    horizontal = rnd.nextBoolean();
    if (horizontal) {
      splitAreaHorizontal();
    } else {
      splitAreaVertical();
    }

    if (leftAreaNode != null && (leftAreaNode.isWidthInRange() || leftAreaNode.isHeightInRange())) {
      leftAreaNode.splitArea();
    } else {
      isLeaf = true;
    }

    if (rightAreaNode != null && (rightAreaNode.isWidthInRange()
        || rightAreaNode.isHeightInRange())) {
      rightAreaNode.splitArea();
    } else {
      isLeaf = true;
    }

    if (isLeaf) {
      bspLeaf = new BSPLeaf(new Position(y, x), width - 2, height - 2, this);
    }
  }

  private void splitAreaHorizontal() {
    int split = calculateSplit(height, configuration.getHeight());
    int counter = 0;
    while (split == 0 && counter < 8) {
      counter++;
      split = calculateSplit(height, configuration.getHeight());
    }
    if (split == 0 && horizontal) {
      splitAreaVertical();
    }
    if (split > configuration.getWidth()) {
      leftAreaNode = new BSPArea(x, y, width, split, rnd, configuration);
      rightAreaNode = new BSPArea(x, y + split - 1, width, height - split + 1, rnd, configuration);
    }
  }

  private void splitAreaVertical() {
    int split = calculateSplit(width, configuration.getWidth());
    int counter = 0;
    while (split == 0 && counter < 8) {
      counter++;
      split = calculateSplit(width, configuration.getWidth());
    }
    if (split == 0 && !horizontal) {
      splitAreaHorizontal();
    }
    if (split > configuration.getWidth()) {
      leftAreaNode = new BSPArea(x, y, split, height, rnd, configuration);
      rightAreaNode = new BSPArea(x + split - 1, y, width - split + 1, height, rnd,
          configuration);
    }
  }

  private int calculateSplit(int size, int minSize) {
    int split;
    if (size > minSize) {
      split = rnd.nextInt(size - minSize) + minSize;
    } else {
      return 0;
    }
    if (size - split < minSize) {
      return 0;
    }
    return split;
  }

  private boolean isWidthInRange() {
    return width > configuration.getWidth();
  }

  private boolean isHeightInRange() {
    return height > configuration.getHeight();
  }

  public BSPArea getLeftAreaNode() {
    return leftAreaNode;
  }

  public BSPArea getRightAreaNode() {
    return rightAreaNode;
  }

  public List<BSPLeaf> getLeaves(List<BSPLeaf> roomList) {

    if (bspLeaf != null) {
      roomList.add(bspLeaf);
    }
    if (leftAreaNode != null) {
      roomList = leftAreaNode.getLeaves(roomList);
    }

    if (rightAreaNode != null) {
      roomList = rightAreaNode.getLeaves(roomList);
    }

    return roomList;
  }
}

