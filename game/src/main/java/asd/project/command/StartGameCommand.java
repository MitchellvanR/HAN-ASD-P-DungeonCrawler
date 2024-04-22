package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.exceptions.ClientGameStartException;
import java.util.Random;
import java.util.UUID;

public class StartGameCommand extends Command {

  private final Random random = new Random();

  public StartGameCommand(Game game) {
    super(game, CommandConfiguration.START_GAME);
  }

  @Override
  public void perform(String argument) {
    int worldSeed;

    if (!argument.isEmpty()) {
      try {
        worldSeed = Integer.parseInt(argument);
      } catch (NumberFormatException exception) {
        game.getUI().updateMessage(CommandError.ERROR_INVALID_SEED.toMessage());
        return;
      }
    } else {
      worldSeed = random.nextInt();
    }
    EntityConfig entityConfig = new EntityConfig();
    entityConfig = entityConfig.readEntityConfig();
    UUID chunkUUID = UUID.randomUUID();
    var startGameDTO = new StartGameDTO(game.getWorld().getChunkWidth(),
        game.getWorld().getChunkHeight(), worldSeed, chunkUUID);
    try {
      game.getNetwork().setEntityConfig(entityConfig);
      game.getNetwork().startGame(startGameDTO);
    } catch (ClientGameStartException e) {
      game.getUI().updateMessage(CommandError.ERROR_CLIENT_STARTGAME.toMessage());
    }
  }
}
