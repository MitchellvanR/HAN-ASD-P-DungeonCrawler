package asd.project.statepainter;

import asd.project.Viewport;
import asd.project.domain.Grid;
import asd.project.domain.Position;
import asd.project.domain.dto.ui.UIActiveStateDTO;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.domain.entity.Entity;
import asd.project.domain.entity.Player;
import java.awt.Color;
import java.awt.Graphics;

public class ActiveStatePainter implements IStatePainter {

  @Override
  public void paintComponent(Graphics graphics, UIStateDTO stateDTO) {
    var activeStateDTO = (UIActiveStateDTO) stateDTO;
    var grid = getViewportGrid(activeStateDTO.getPlayer());
    var fontMetrics = graphics.getFontMetrics();

    graphics.setColor(Color.WHITE);

    for (int i = 0; i < grid.getHeight(); i++) {
      var stringBuilder = new StringBuilder();

      for (int j = 0; j < grid.getWidth(); j++) {
        var tile = grid.getTile(new Position(j, i));
        var optionalEntity = tile.getEntity();
        var character = optionalEntity.map(Entity::getCharacter).orElseGet(tile::getCharacter);

        stringBuilder.append(character);
      }

      graphics.drawString(stringBuilder.toString(), fontMetrics.getDescent(),
          i * fontMetrics.getHeight() + fontMetrics.getAscent());
    }
  }

  private Grid getViewportGrid(Player player) {
    var position = calculateViewportPosition(player);
    var grid = player.getChunk().getGrid();

    return grid.takeAreaUnsafe(position, Viewport.VIEWPORT_WIDTH, Viewport.VIEWPORT_HEIGHT);
  }

  private Position calculateViewportPosition(Player player) {
    var position = player.getPosition();
    int widthPos = position.getX() / Viewport.VIEWPORT_WIDTH * Viewport.VIEWPORT_WIDTH;
    int heightPos = position.getY() / Viewport.VIEWPORT_HEIGHT * Viewport.VIEWPORT_HEIGHT;

    return new Position(widthPos, heightPos);
  }
}
