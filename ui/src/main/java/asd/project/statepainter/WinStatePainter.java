package asd.project.statepainter;

import asd.project.domain.dto.ui.UIEndState;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.panel.MainPanel;
import java.awt.Color;
import java.awt.Graphics;

public class WinStatePainter implements IStatePainter {

  private final MainPanel mainPanel;

  public WinStatePainter(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
  }

  @Override
  public void paintComponent(Graphics graphics, UIStateDTO stateDTO) {
    var uiWinStateDTO = (UIEndState) stateDTO;
    var fontMetrics = graphics.getFontMetrics();
    var generalHeight = mainPanel.getHeight() / 2;
    var generalWidth = mainPanel.getWidth() / 2;
    var fontHeight = fontMetrics.getHeight() / 2;
    var wonMessage = uiWinStateDTO.getPromptList().get(0);
    var healthMessage = uiWinStateDTO.getPromptList().get(1);
    var moneyMessage = uiWinStateDTO.getPromptList().get(2);

    graphics.setColor(Color.GREEN);
    graphics.drawString(wonMessage,
        generalWidth - fontMetrics.stringWidth(wonMessage) / 2,
        generalHeight - fontHeight);
    graphics.setColor(Color.WHITE);
    graphics.drawString(healthMessage,
        generalWidth - fontMetrics.stringWidth(healthMessage) / 2,
        generalHeight - fontHeight + 50);
    graphics.drawString(moneyMessage,
        generalWidth - fontMetrics.stringWidth(moneyMessage) / 2,
        generalHeight - fontHeight + 75);
  }
}
