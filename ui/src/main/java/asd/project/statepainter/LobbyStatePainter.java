package asd.project.statepainter;

import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.ui.UIConfigurationStateDTO;
import asd.project.domain.dto.ui.UILobbyStateDTO;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.panel.MainPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class LobbyStatePainter extends ActiveStatePainter implements IStatePainter {

  private final MainPanel mainPanel;
  private EntityConfig config;

  public LobbyStatePainter(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
    this.config = new EntityConfig();
  }

  @Override
  public void paintComponent(Graphics graphics, UIStateDTO stateDTO) {
    config = config.readEntityConfig();
    var configurationStateDTO = (UIConfigurationStateDTO) stateDTO;
    var prompts = configurationStateDTO.getPromptList();

    if (!prompts.isEmpty()) {
      printText(graphics, prompts);
    }

    var uiLobbyStateDTO = (UILobbyStateDTO) stateDTO;
    var fontMetrics = graphics.getFontMetrics();
    var roomCode = uiLobbyStateDTO.getRoomCode();
    var configMessage = "To change a variable type [/{variable name} {new value}]";
    var startGameMessage = "Type [/start game] to start the game";
    int margin = 50;
    int flagMargin = margin + 50;
    int xPos = 50;
    int textMargin = 10;
    int xPos2 = mainPanel.getWidth() - margin;
    int yPos = mainPanel.getHeight() / 3 + margin + 30;
    int yPos2 = mainPanel.getHeight() - margin;
    int colomX = (xPos2 - margin) / 3;
    int colomY = 50;
    int roomCodeX = 265;

    //to write te room code
    graphics.setColor(Color.WHITE);
    graphics.drawString("Room code: " + roomCode,
        margin,
        roomCodeX - fontMetrics.getHeight() / 2);

    graphics.setColor(Color.WHITE);
    graphics.drawString("Variable", xPos, yPos - 5);
    graphics.drawString("Value", xPos + colomX, yPos - 5);
    graphics.drawString("Options", xPos + (colomX * 2), yPos - 5);
    graphics.drawString("difficulty",
        margin + textMargin,
        yPos + margin);

    graphics.drawString(config.getDifficulty(),
        xPos + colomX + textMargin,
        yPos + margin);
    graphics.drawString("easy, normal, hard",
        xPos + (colomX * 2) + textMargin,
        yPos + margin);

    graphics.drawString("item count",
        margin + textMargin,
        yPos + margin + colomY);

    graphics.drawString(Integer.toString(config.getItemCount()),
        xPos + colomX + textMargin,
        yPos + margin + colomY);
    graphics.drawString("10 t/m 50",
        xPos + (colomX * 2) + textMargin,
        yPos + margin + colomY);

    graphics.drawString("flag count",
        margin + textMargin,
        yPos + flagMargin + colomY);

    graphics.drawString(Integer.toString(config.getFlagCount()),
        xPos + colomX + textMargin,
        yPos + flagMargin + colomY);
    graphics.drawString("0 t/m 5",
        xPos + (colomX * 2) + textMargin,
        yPos + flagMargin + colomY);

    graphics.setColor(Color.WHITE);
    graphics.drawString("Variable", xPos, yPos - 5);
    graphics.drawString("Value", xPos + colomX, yPos - 5);
    graphics.drawString("Options", xPos + (colomX * 2), yPos - 5);

    //to write te command tip
    graphics.setColor(Color.green);
    graphics.drawString(configMessage,
        10,
        mainPanel.getHeight() - 20 - fontMetrics.getHeight() / 2);

    graphics.setColor(Color.green);
    graphics.drawString(startGameMessage,
        10,
        mainPanel.getHeight() + 5 - fontMetrics.getHeight() / 2);

    graphics.drawLine(xPos, yPos, xPos2, yPos);
    graphics.drawLine(xPos + colomX, yPos, xPos + colomX, yPos2);
    graphics.drawLine(xPos + (colomX * 2), yPos, xPos + (colomX * 2), yPos2);
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
