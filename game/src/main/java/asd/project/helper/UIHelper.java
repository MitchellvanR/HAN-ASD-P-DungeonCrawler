package asd.project.helper;

import asd.project.Game;
import asd.project.state.FindNewHostState;
import asd.project.command.CommandError;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.ui.UIDeathStateDTO;
import asd.project.domain.entity.Player;
import asd.project.state.EndState;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIHelper extends Helper {

  private static final Logger LOGGER = Logger.getLogger(UIHelper.class.getName());

  public UIHelper(Game game) {
    super(game);
  }

  public void runCommandFromUI(String command) {
    var optionalCommandFromList = game.getState().getCommandFromList(command);

    optionalCommandFromList.ifPresentOrElse(commandFromList -> {
      var argument = commandFromList.getArgument(command);
      commandFromList.perform(argument);
    }, () -> {
      if (!(game.getState() instanceof FindNewHostState)) {
        game.getUI().updateMessage(CommandError.ERROR_INVALID_COMMAND.toMessage());
      } else {
        game.getUI().updateMessage(CommandError.INFO_COMMAND_USAGE_WHILE_SEARCHING_HOST.toWarning());
      }

    });
  }

  public void sendMessageToNetwork(String message) {
    game.getNetwork().sendMessage(new MessageDTO(game.getUsername() + ": " + message));
  }

  public void updateUIStats(Player player) {
    if (player.getHealth() <= 0) {
      handlePlayerDeath(player);
    }
    game.getUI().updateUserStats(player);
  }

  public void handlePlayerDeath(Player player) {
    LOGGER.log(Level.INFO, "Player died!: {}.", player.getUUID());
    game.getStorage().deletePlayer(player);
    game.setState(new EndState(game));
    game.getUI().setStatePainter(new UIDeathStateDTO(player));

    GameStateDTO gameStateDTO = new GameStateDTO(player.getPosition().getX(),
        player.getPosition().getY(), player.getUUID(), null,
        player.getPower(), player.getHealth(), player.getMoney(), player.getFlagCount());
    game.getNetwork().sendGameState(gameStateDTO);

    int knightsAlive = game.getStorage().getAmountOfKnightsAlive();
    game.getNetwork().sendMessage(new MessageDTO(game.getUsername() + " died. " + knightsAlive
        + " players remaining.", Color.BLUE));
  }
}
