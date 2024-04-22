package asd.project.state;

import asd.project.Game;
import asd.project.command.ConfigureAgentCommand;
import asd.project.command.HostGameCommand;
import asd.project.command.JoinLobbyCommand;
import asd.project.command.RejoinGameCommand;

public class StartState extends State {

  public StartState(Game game) {
    commandList.add(new HostGameCommand(game));
    commandList.add(new RejoinGameCommand(game));
    commandList.add(new JoinLobbyCommand(game));
    commandList.add(new ConfigureAgentCommand(game));
  }
}
