package asd.project.state;

import asd.project.Game;
import asd.project.command.ChangeFlagAmountCommand;
import asd.project.command.ChangeItemAmountCommand;
import asd.project.command.ChangeMonsterDifficultyCommand;
import asd.project.command.StartGameCommand;

public class LobbyState extends State {

  public LobbyState(Game game) {
    commandList.add(new StartGameCommand(game));
    commandList.add(new ChangeMonsterDifficultyCommand(game));
    commandList.add(new ChangeItemAmountCommand(game));
    commandList.add(new ChangeFlagAmountCommand(game));

  }

}
