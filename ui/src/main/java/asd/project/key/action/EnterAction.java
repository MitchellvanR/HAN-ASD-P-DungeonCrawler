package asd.project.key.action;

import asd.project.domain.event.Event;
import asd.project.domain.event.EventType;
import asd.project.key.GameKeyListener;

public class EnterAction implements KeyAction {

  @Override
  public String perform(GameKeyListener gameKeyListener, String characters, char character) {
    if (characters.isEmpty() || characters.isBlank()) {
      return characters;
    }

    var ui = gameKeyListener.getUI();
    var navigationHandler = gameKeyListener.getNavigationHandler();

    EventType eventType;

    if (characters.startsWith("/")) {
      eventType = EventType.UI_COMMAND;
    } else {
      eventType = EventType.UI_MESSAGE;
    }

    Event event = new Event(eventType, characters);
    ui.publish(event);

    navigationHandler.setMessageIndex(-1);
    navigationHandler.getMessages().add(0, characters);

    return "";
  }
}