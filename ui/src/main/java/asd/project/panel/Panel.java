package asd.project.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Abstract class for the base configuration of all panels
 */
public abstract class Panel extends JPanel {

  protected Panel(int width, int height) {
    var font = new Font("Ubuntu Mono", Font.PLAIN, 24);

    setPreferredSize(new Dimension(width, height));
    setFont(font);
    setBackground(Color.BLACK);
    setBorder(BorderFactory.createLineBorder(Color.white));
  }
}
