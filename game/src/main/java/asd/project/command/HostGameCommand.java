package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.domain.dto.ui.UIHostGameStateDTO;
import asd.project.state.HostGameState;

public class HostGameCommand extends Command {

  public HostGameCommand(Game game) {
    super(game, CommandConfiguration.HOST);
  }

  @Override
  public void perform(String argument) {
    game.getUI().setStatePainter(new UIHostGameStateDTO());
    game.setState(new HostGameState(game));
  }
}
