package asd.project.key.action;

import asd.project.key.GameKeyListener;

public class BackAction implements KeyAction {

  @Override
  public String perform(GameKeyListener gameKeyListener, String characters, char character) {
    return characters.substring(0, Math.max(characters.length() - 1, 0));
  }
}
