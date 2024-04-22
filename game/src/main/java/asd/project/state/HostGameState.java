package asd.project.state;

import asd.project.Game;
import asd.project.command.NewGameCommand;

public class HostGameState extends State {

  public HostGameState(Game game) {
    commandList.add(new NewGameCommand(game));
  }

}
