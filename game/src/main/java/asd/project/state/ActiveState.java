package asd.project.state;

import asd.project.Game;
import asd.project.command.LeaveCommand;
import asd.project.command.MoveDownCommand;
import asd.project.command.MoveLeftCommand;
import asd.project.command.MoveRightCommand;
import asd.project.command.MoveUpCommand;
import asd.project.command.PauseGameCommand;

public class ActiveState extends State {

  public ActiveState(Game game) {
    commandList.add(new MoveUpCommand(game));
    commandList.add(new MoveLeftCommand(game));
    commandList.add(new MoveDownCommand(game));
    commandList.add(new MoveRightCommand(game));
    commandList.add(new LeaveCommand(game));
    commandList.add(new PauseGameCommand(game));
  }
}
