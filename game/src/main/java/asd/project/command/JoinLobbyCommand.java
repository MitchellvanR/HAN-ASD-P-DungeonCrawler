package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.ui.UILobbyStateDTO;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.state.LobbyState;
import java.net.ConnectException;

public class JoinLobbyCommand extends Command {

  private EntityConfig entityConfig;

  public JoinLobbyCommand(Game game) {
    super(game, CommandConfiguration.JOIN_LOBBY);
    entityConfig = new EntityConfig(20, 50, 0);
  }

  @Override
  public void perform(String argument) {
    var argumentArray = argument.split(" ", 2);
    var ui = game.getUI();

    if (argumentArray.length != 2) {
      ui.updateMessage(CommandError.ERROR_INVALID_ARGUMENTS.toMessage());

      return;
    }

    var roomCode = argumentArray[0].toUpperCase();
    var username = argumentArray[1];

    try {
      boolean internet = game.getNetwork().joinGame(roomCode);
      game.getNetwork().setRoomCode(roomCode);
      game.setRoomCode(roomCode);

      if (internet) {
        ui.updateUsername(username);
        game.setUsername(username);
        entityConfig.setConfig(entityConfig);
        ui.setStatePainter(new UILobbyStateDTO(roomCode));
        game.setState(new LobbyState(game));
      }
    } catch (ConnectException cex) {
      ui.updateMessage(CommandError.ERROR_IP_CONNECTION.toMessage());
    } catch (InvalidRoomCodeException iex) {
      ui.updateMessage(CommandError.ERROR_INVALID_CODE.toMessage());
    }
  }
}
