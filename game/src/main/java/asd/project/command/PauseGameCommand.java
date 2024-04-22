package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;

public class PauseGameCommand extends Command {

  public PauseGameCommand(Game game) {
    super(game, CommandConfiguration.PAUSE_GAME);
  }

  @Override
  public void perform(String argument) {
    game.getNetwork().pauseGame();
  }
}