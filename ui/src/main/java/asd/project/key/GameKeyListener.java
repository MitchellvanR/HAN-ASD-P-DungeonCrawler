package asd.project.key;

import asd.project.UI;
import asd.project.key.action.BackAction;
import asd.project.key.action.DefaultAction;
import asd.project.key.action.EnterAction;
import asd.project.key.action.KeyAction;
import asd.project.key.action.navigation.DownAction;
import asd.project.key.action.navigation.NavigationHandler;
import asd.project.key.action.navigation.UpAction;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {

  private final UI ui;
  private final NavigationHandler navigationHandler;
  private String characters;

  public GameKeyListener(UI ui) {
    this.ui = ui;
    characters = "";
    navigationHandler = new NavigationHandler();
  }

  @Override
  public void keyTyped(KeyEvent keyEvent) {
  }

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    int keyCode = keyEvent.getKeyCode();
    char character = keyEvent.getKeyChar();
    KeyAction keyAction;

    switch (keyCode) {
      case KeyEvent.VK_ENTER -> keyAction = new EnterAction();
      case KeyEvent.VK_BACK_SPACE -> keyAction = new BackAction();
      case KeyEvent.VK_UP -> keyAction = new UpAction();
      case KeyEvent.VK_DOWN -> keyAction = new DownAction();
      default -> keyAction = new DefaultAction();
    }

    characters = keyAction.perform(this, characters, character);
    ui.repaint();
  }

  @Override
  public void keyReleased(KeyEvent keyEvent) {
  }

  public UI getUI() {
    return ui;
  }

  public String getCharacters() {
    return characters;
  }

  public NavigationHandler getNavigationHandler() {
    return navigationHandler;
  }
}
