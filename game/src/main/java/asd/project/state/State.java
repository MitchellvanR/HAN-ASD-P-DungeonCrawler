package asd.project.state;

import asd.project.command.Command;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The state of the game.
 */
public abstract class State {

  protected final Set<Command> commandList;

  protected State() {
    this.commandList = new HashSet<>();
  }

  public Optional<Command> getCommandFromList(String name) {
    return commandList.stream()
        .filter(
            command -> command.getName().equals(name) || name.startsWith(command.getName() + " "))
        .findAny();
  }
}
