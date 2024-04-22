package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.config.world.EntityConfig;

public class ChangeFlagAmountCommand extends Command {

  private EntityConfig entityConfig;

  public ChangeFlagAmountCommand(Game game) {
    super(game, CommandConfiguration.FLAG_COUNT);
    this.entityConfig = new EntityConfig();
  }

  @Override
  public void perform(String argument) {
    entityConfig = entityConfig.readEntityConfig();
    int flagCount = 0;
    try {
      flagCount = Integer.parseInt(argument);
    } catch (NumberFormatException exception) {
      game.getUI().updateMessage(CommandError.ERROR_INVALID_ARGUMENTS.toMessage());
    }
    entityConfig.setFlagCount(flagCount);
    game.getNetwork().setEntityConfig(entityConfig);
  }

}
