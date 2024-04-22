package asd.project.panel;

import asd.project.UI;
import asd.project.key.GameKeyListener;
import java.awt.Color;
import java.awt.Graphics;

public class InputPanel extends Panel {

  private final GameKeyListener gameKeyListener;

  public InputPanel(int width, int height, UI ui) {
    super(width, height);
    gameKeyListener = new GameKeyListener(ui);

    setFocusable(true);
    addKeyListener(gameKeyListener);
  }

  @Override
  public final void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    graphics.setColor(Color.WHITE);

    var fontMetrics = graphics.getFontMetrics();

    graphics.drawString(gameKeyListener.getCharacters(), fontMetrics.getDescent(),
        fontMetrics.getAscent());
  }
}
