package asd.project.key.action;

import asd.project.key.GameKeyListener;

/**
 * Interface for performing key actions.
 */
public interface KeyAction {

  /**
   * After a key is pressed in the GameKeyListener an implementation of this interface will be
   * executed.
   *
   * @param gameKeyListener: the key listener.
   * @param characters:      the characters that are pressed before.
   * @param character:       the character that is pressed.
   * @return the new calculated characters.
   */
  String perform(GameKeyListener gameKeyListener, String characters, char character);
}
