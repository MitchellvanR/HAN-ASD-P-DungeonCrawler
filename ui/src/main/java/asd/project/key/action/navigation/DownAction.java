package asd.project.key.action.navigation;

import asd.project.key.GameKeyListener;
import asd.project.key.action.KeyAction;

public class DownAction implements KeyAction {

  @Override
  public String perform(GameKeyListener gameKeyListener, String characters, char character) {
    var navigationHandler = gameKeyListener.getNavigationHandler();
    int messageIndex = navigationHandler.getMessageIndex();

    if (messageIndex <= 0) {
      return characters;
    }

    characters = navigationHandler.getMessages().get(--messageIndex);
    navigationHandler.setMessageIndex(messageIndex);

    return characters;
  }
}
