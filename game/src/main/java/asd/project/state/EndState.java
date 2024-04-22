package asd.project.state;

import asd.project.Game;
import asd.project.command.LeaveCommand;

public class EndState extends State {

  public EndState(Game game) {
    commandList.add(new LeaveCommand(game));
  }
}
