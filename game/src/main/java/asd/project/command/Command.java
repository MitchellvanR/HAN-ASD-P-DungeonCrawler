package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;

/**
 * A command is linked to a State. Each State has multiple commands.
 */
public abstract class Command {

  protected final Game game;
  private final String name;

  protected Command(Game game, CommandConfiguration commandConfiguration) {
    this.game = game;
    this.name = "/" + commandConfiguration;
  }

  /**
   * This method wil be executed when given input is equal to the name or one of the aliases of the
   * command.
   */
  public abstract void perform(String argument);

  public String getName() {
    return name;
  }

  public String getArgument(String command) {
    if (command.equals(name)) {
      return "";
    }

    return command.replace(name + " ", "");
  }
}
