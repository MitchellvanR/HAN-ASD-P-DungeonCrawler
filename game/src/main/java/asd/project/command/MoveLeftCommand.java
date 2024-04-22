package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.domain.Position;
import asd.project.domain.dto.serializable.GameStateDTO;

public class MoveLeftCommand extends Command {

  public MoveLeftCommand(Game game) {
    super(game, CommandConfiguration.MOVE_LEFT);
  }

  @Override
  public void perform(String argument) {
    var world = game.getWorld();
    var player = world.getPlayer();
    var playerChunk = player.getChunk();
    var newPosition = new Position(player.getPosition().getX() - 1, player.getPosition().getY());

    game.getNetwork().sendGameState(
        new GameStateDTO(newPosition.getX(), newPosition.getY(), player.getUUID(),
            playerChunk.getUUID(), player.getPower(), player.getHealth(), player.getMoney(),
            player.getFlagCount()));
  }
}
