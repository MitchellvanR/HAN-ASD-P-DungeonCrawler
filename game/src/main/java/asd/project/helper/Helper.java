package asd.project.helper;

import asd.project.Game;

/**
 * Base class for helper classes. Subclasses must implement their own helper methods or
 * functionalities by extending this class.
 */
public abstract class Helper {

  protected final Game game;

  /**
   * Constructs a Helper instance with the specified Game instance.
   *
   * @param game The Game instance which is associated with this helper class.
   */
  protected Helper(Game game) {
    this.game = game;
  }
}
