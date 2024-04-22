package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.domain.dto.ui.UIActiveStateDTO;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.state.ActiveState;
import java.net.ConnectException;

public class RejoinGameCommand extends Command {

  public RejoinGameCommand(Game game) {
    super(game, CommandConfiguration.REJOIN_GAME);
  }

  @Override
  public void perform(String argument) {
    var player = game.getWorld().getPlayer();
    var ui = game.getUI();

    try {
      boolean internet = game.getNetwork().joinGame(argument);
      game.getNetwork().setRoomCode(argument);
      //If connection succeeds, proceed to the LobbyState.
      if (internet) {
        ui.setStatePainter(new UIActiveStateDTO(player));
        game.setState(new ActiveState(game));
      }
    } catch (ConnectException cex) {
      game.getUI().updateMessage(CommandError.ERROR_IP_CONNECTION.toMessage());
    } catch (InvalidRoomCodeException iex) {
      game.getUI().updateMessage(CommandError.ERROR_INVALID_CODE.toMessage());
    }
  }
}
