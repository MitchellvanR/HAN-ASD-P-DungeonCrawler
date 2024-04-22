package asd.project.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserStatsPanel extends Panel {

  private String userDisplay = "null";
  private String healthDisplay = "0";
  private String moneyDisplay = "0";
  private String attributeDisplay = "{}";

  public UserStatsPanel(int width, int height) {
    super(width, height);
  }

  @Override
  public final void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    var fontMetrics = graphics.getFontMetrics();

    LinkedHashMap<String, Color> statsText = new LinkedHashMap<>();
    statsText.put("@: " + userDisplay, Color.GREEN);
    statsText.put("+: " + healthDisplay, Color.RED);
    statsText.put("€: " + moneyDisplay, Color.YELLOW);
    statsText.put("Ω: " + attributeDisplay, Color.CYAN);

    int index = 0;

    for (Map.Entry<String, Color> entry : statsText.entrySet()) {
      graphics.setColor(entry.getValue());
      graphics.drawString(entry.getKey(), fontMetrics.getDescent(),
          fontMetrics.getHeight() + (fontMetrics.getHeight() * index));
      index++;
    }
  }

  public void setUserDisplay(String userDisplay) {
    this.userDisplay = userDisplay != null ? userDisplay : this.userDisplay;
  }

  public void setHealthDisplay(String healthDisplay) {
    this.healthDisplay = healthDisplay;
  }

  public void setMoneyDisplay(String moneyDisplay) {
    this.moneyDisplay = moneyDisplay;
  }

  public void setAttributeDisplay(String attributeDisplay) {
    this.attributeDisplay = attributeDisplay;
  }
}