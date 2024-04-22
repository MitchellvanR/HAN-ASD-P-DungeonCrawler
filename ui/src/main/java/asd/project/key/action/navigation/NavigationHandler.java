package asd.project.key.action.navigation;

import java.util.LinkedList;
import java.util.List;

public class NavigationHandler {

  private final LinkedList<String> messages;
  private int messageIndex;

  public NavigationHandler() {
    messageIndex = -1;
    messages = new LinkedList<>();
  }

  public int getMessageIndex() {
    return messageIndex;
  }

  public void setMessageIndex(int messageIndex) {
    this.messageIndex = messageIndex;
  }

  public List<String> getMessages() {
    return messages;
  }
}
