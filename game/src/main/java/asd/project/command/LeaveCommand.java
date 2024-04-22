package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.domain.dto.ui.UIStartStateDTO;
import asd.project.state.StartState;

public class LeaveCommand extends Command {

  public LeaveCommand(Game game) {
    super(game, CommandConfiguration.LEAVE_GAME);
  }

  @Override
  public void perform(String argument) {
    game.getNetwork().leaveGame();

    game.setState(new StartState(game));
    game.getUI().setStatePainter(new UIStartStateDTO());
  }
}
