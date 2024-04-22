package asd.project.key.action.navigation;

import asd.project.key.GameKeyListener;
import asd.project.key.action.KeyAction;

public class UpAction implements KeyAction {

  @Override
  public String perform(GameKeyListener gameKeyListener, String characters, char character) {
    var navigationHandler = gameKeyListener.getNavigationHandler();
    var navigationCharacters = navigationHandler.getMessages();
    var messageIndex = navigationHandler.getMessageIndex();

    if (navigationCharacters.isEmpty() || navigationCharacters.size() - 1 == messageIndex) {
      return characters;
    }

    characters = navigationCharacters.get(++messageIndex);
    navigationHandler.setMessageIndex(messageIndex);

    return characters;
  }
}
