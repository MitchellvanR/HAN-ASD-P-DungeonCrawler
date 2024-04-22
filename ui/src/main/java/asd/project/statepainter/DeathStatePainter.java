package asd.project.statepainter;

import asd.project.domain.dto.ui.UIDeathStateDTO;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.panel.MainPanel;
import java.awt.Color;
import java.awt.Graphics;

public class DeathStatePainter implements IStatePainter {

  private final MainPanel mainPanel;

  public DeathStatePainter(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
  }

  @Override
  public void paintComponent(Graphics graphics, UIStateDTO stateDTO) {
    var uiDeathStateDTO = (UIDeathStateDTO) stateDTO;
    var fontMetrics = graphics.getFontMetrics();
    var diedMessage = uiDeathStateDTO.getPromptList().get(0);

    graphics.setColor(Color.WHITE);
    graphics.drawString(diedMessage,
        mainPanel.getWidth() / 2 - fontMetrics.stringWidth(diedMessage) / 2,
        mainPanel.getHeight() / 2 - fontMetrics.getHeight() / 2);
  }
}
