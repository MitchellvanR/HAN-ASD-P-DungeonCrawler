package asd.project.command;

import asd.project.Game;
import asd.project.config.CommandConfiguration;
import asd.project.config.world.EntityConfig;
import asd.project.domain.dto.ui.UILobbyStateDTO;
import asd.project.exceptions.CreateRoomException;
import asd.project.state.LobbyState;

public class NewGameCommand extends Command {

  private EntityConfig entityConfig;

  public NewGameCommand(Game game) {
    super(game, CommandConfiguration.NEW_GAME);
    entityConfig = new EntityConfig(20, 50, 0);
  }

  @Override
  public void perform(String argument) {
    var ui = game.getUI();

    if (argument.isEmpty() || argument.isBlank()) {
      ui.updateMessage(CommandError.ERROR_INVALID_NAME.toMessage());

      return;
    }
    ui.updateUsername(argument);

    try {
      var roomCode = game.getNetwork().openServer();
      game.setRoomCode(roomCode);
      game.setUsername(argument);
      entityConfig.setConfig(entityConfig);
      if (roomCode != null) {
        ui.setStatePainter(new UILobbyStateDTO(roomCode));
        game.setState(new LobbyState(game));
      }
    } catch (CreateRoomException e) {
      game.getUI().updateMessage(CommandError.ERROR_CREATE_ROOM.toMessage());
    }
  }

  public void setEntityConfig(EntityConfig entityConfig) {
    this.entityConfig = entityConfig;
  }

}
