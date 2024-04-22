package asd.project.key.action;

import asd.project.key.GameKeyListener;

public class DefaultAction implements KeyAction {

  @Override
  public String perform(GameKeyListener gameKeyListener, String characters, char character) {
    return isAllowedCharacter(character) ? characters + character : characters;
  }

  private boolean isAllowedCharacter(char targetCharacter) {
    return String.valueOf(targetCharacter).matches("^[a-zA-Z0-9 ?/!]+$");
  }
}
