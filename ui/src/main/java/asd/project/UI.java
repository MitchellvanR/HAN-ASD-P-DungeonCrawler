package asd.project;

import asd.project.domain.Message;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.domain.entity.CharacterEntity;
import asd.project.panel.ChatPanel;
import asd.project.panel.InputPanel;
import asd.project.panel.MainPanel;
import asd.project.panel.UserStatsPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UI extends EventObservable implements IUI {

  private static final Logger LOGGER = Logger.getLogger(UI.class.getName());
  private ChatPanel chatPanel;
  private MainPanel mainPanel;
  private InputPanel inputPanel;
  private UserStatsPanel userStatsPanel;

  public UI() {
    registerCustomFont();
    createPanels();
    addPanelsToJFrame();
  }

  private void registerCustomFont() {
    try {
      GraphicsEnvironment.getLocalGraphicsEnvironment()
          .registerFont(
              Font.createFont(Font.TRUETYPE_FONT, new File("resources/UbuntuMono-Regular.ttf")));
    } catch (FontFormatException | IOException e) {
      LOGGER.log(Level.INFO, "Can not find the font at resources/UbuntuMono-Regular.ttf");
    }
  }

  public void createPanels() {
    userStatsPanel = new UserStatsPanel(254, 106);
    chatPanel = new ChatPanel(0, 500);
    mainPanel = new MainPanel(972, 606);
    inputPanel = new InputPanel(0, 30, this);

  }

  public void addPanelsToJFrame() {
    var leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.add(mainPanel);
    leftPanel.add(inputPanel);

    var rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.add(userStatsPanel);
    rightPanel.add(chatPanel);

    var jFrame = new JFrame();
    try {
      jFrame.setIconImage(ImageIO.read(new File("resources/appIcon.jpeg")));
    } catch (IOException e) {
      LOGGER.log(Level.INFO, "Can not find the appIcon at resources/appIcon.jpeg");
    }
    jFrame.setTitle("DungeonGame");
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.setResizable(false);
    jFrame.setLayout(new BorderLayout());
    jFrame.add(leftPanel, BorderLayout.CENTER);
    jFrame.add(rightPanel, BorderLayout.LINE_END);
    jFrame.pack();
    jFrame.setLocationRelativeTo(null);
    jFrame.setVisible(true);
  }

  @Override
  public void setStatePainter(UIStateDTO stateDTO) {
    mainPanel.setStatePainter(stateDTO);
    mainPanel.repaint();
  }

  @Override
  public void setStateDTO(UIStateDTO stateDTO) {
    mainPanel.setStateDTO(stateDTO);
    mainPanel.repaint();
  }

  @Override
  public void updateMessage(Message message) {
    chatPanel.addMessage(message);
    chatPanel.repaint();
  }

  @Override
  public void updateUserStats(CharacterEntity characterEntity) {
    userStatsPanel.setHealthDisplay(String.valueOf(characterEntity.getHealth()));
    userStatsPanel.setMoneyDisplay(String.valueOf(characterEntity.getMoney()));
    userStatsPanel.setAttributeDisplay(
        "€*:" + characterEntity.getMoneyRate() + "; Power*:" + characterEntity.getPowerRate());
    userStatsPanel.repaint();
  }

  @Override
  public void updateUsername(String name) {
    userStatsPanel.setUserDisplay(name);
  }

  public void repaint() {
    mainPanel.repaint();
    chatPanel.repaint();
    inputPanel.repaint();
    userStatsPanel.repaint();
  }
}
