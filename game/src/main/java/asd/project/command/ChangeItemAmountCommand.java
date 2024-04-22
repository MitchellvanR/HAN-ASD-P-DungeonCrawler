package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.config.world.EntityConfig;

public class ChangeItemAmountCommand extends Command {

  private EntityConfig entityConfig;

  public ChangeItemAmountCommand(Game game) {
    super(game, CommandConfiguration.ITEM_COUNT);
    this.entityConfig = new EntityConfig();
  }

  @Override
  public void perform(String argument) {
    entityConfig = entityConfig.readEntityConfig();
    int itemCount = 0;
    try {
      itemCount = Integer.parseInt(argument);
    } catch (NumberFormatException e) {
      game.getUI().updateMessage(CommandError.ERROR_INVALID_ARGUMENTS.toMessage());
    }
    entityConfig.setItemCount(itemCount);
    game.getNetwork().setEntityConfig(entityConfig);
  }

  void setEntityConfig(EntityConfig entityConfig) {
    this.entityConfig = entityConfig;
  }

}

