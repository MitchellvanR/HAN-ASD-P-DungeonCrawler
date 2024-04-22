package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.config.world.EntityConfig;

public class ChangeMonsterDifficultyCommand extends Command {

  private EntityConfig entityConfig;

  public ChangeMonsterDifficultyCommand(Game game) {
    super(game, CommandConfiguration.DIFFICULTY);
    this.entityConfig = new EntityConfig();
  }

  @Override
  public void perform(String argument) {
    entityConfig = entityConfig.readEntityConfig();
    if (!(argument.equals("easy")) && !(argument.equals("normal")) && !(argument.equals("hard"))) {
      game.getUI()
          .updateMessage(CommandError.ERROR_INVALID_ARGUMENTS.toMessage());
    } else {
      entityConfig.setDifficulty(argument);
      game.getNetwork().setEntityConfig(entityConfig);
    }
  }

  public void setEntityConfig(EntityConfig entityConfig) {
    this.entityConfig = entityConfig;
  }
}
