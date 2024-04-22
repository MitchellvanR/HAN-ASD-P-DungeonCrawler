package asd.project.statepainter;

import asd.project.domain.dto.ui.UIConfigurationStateDTO;
import asd.project.domain.dto.ui.UIStateDTO;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class ConfigurationStatePainter implements IStatePainter {

  @Override
  public void paintComponent(Graphics graphics, UIStateDTO stateDTO) {
    var configurationStateDTO = (UIConfigurationStateDTO) stateDTO;
    var prompts = configurationStateDTO.getPromptList();

    if (!prompts.isEmpty()) {
      printText(graphics, prompts);
    }
  }

  private void printText(Graphics graphics, List<String> lines) {
    final int MARGIN_LEFT = 10;
    final int LINE_HEIGHT = graphics.getFontMetrics().getHeight();

    for (int i = 0; i < lines.size(); i++) {
      var line = lines.get(i);

      graphics.setColor(Color.WHITE);

      if (line.contains("_") || line.contains("/")) {
        graphics.setColor(Color.GREEN);
      }

      graphics.drawString(line, MARGIN_LEFT, LINE_HEIGHT + (LINE_HEIGHT * i));
    }
  }
}
