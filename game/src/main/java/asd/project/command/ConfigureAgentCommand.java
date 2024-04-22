package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;

public class ConfigureAgentCommand extends Command {

  public ConfigureAgentCommand(Game game) {
    super(game, CommandConfiguration.CONFIGURE_AGENT);
  }

  @Override
  public void perform(String argument) {
    game.getAgent().setBehaviour(argument);
  }
}
