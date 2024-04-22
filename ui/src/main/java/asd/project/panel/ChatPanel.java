package asd.project.panel;

import asd.project.domain.Message;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChatPanel extends Panel {

  private static final int CHARACTERS_PER_LINE = 20;
  private final transient LinkedList<Message> messages;

  public ChatPanel(int width, int height) {
    super(width, height);
    messages = new LinkedList<>();
  }

  @Override
  public final void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    var fontMetrics = graphics.getFontMetrics();
    int messageSpacing = 0;

    for (var message : new ArrayList<>(messages)) {
      graphics.setColor(message.color());
      var value = message.value();

      // Create an empty list for holding all messages printed to the chat panel.
      var messageToPrintList = new ArrayList<String>();
      int messageHeight = 0;

      while (value.length() > CHARACTERS_PER_LINE) {
        // Set spacing between alinea's at right position.
        messageSpacing++;
        messageHeight++;

        messageToPrintList.add(value.substring(0, CHARACTERS_PER_LINE));
        value = value.substring(CHARACTERS_PER_LINE);
      }

      if (!value.isEmpty()) {
        messageToPrintList.add(value);
      }

      // Setting underline at right position.
      messageSpacing++;
      messageHeight++;

      // Print out all messages in the list with the right spacing.
      for (int m = 0; m < messageToPrintList.size(); m++) {
        graphics.drawString(messageToPrintList.get(m), fontMetrics.getDescent(),
            m * fontMetrics.getHeight() + fontMetrics.getAscent()
                + (messageSpacing - messageHeight) * fontMetrics.getHeight());
      }

      // Print out the line under the message at the right position.
      graphics.setColor(Color.WHITE);
      graphics.drawLine(fontMetrics.getDescent(),
          messageSpacing * fontMetrics.getHeight() + fontMetrics.getHeight() / 2,
          getWidth() - fontMetrics.getDescent(),
          messageSpacing * fontMetrics.getHeight() + fontMetrics.getHeight() / 2);

      // Increase the message spacing between last and current message.
      messageSpacing++;
    }
  }

  public void addMessage(Message message) {
    messages.addFirst(message);
  }
}
