package asd.project.helper;

import asd.project.Game;
import asd.project.domain.Chunk;
import asd.project.domain.Message;
import asd.project.domain.Position;
import asd.project.domain.dto.serializable.GameStateDTO;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import asd.project.domain.dto.serializable.WinGameDTO;
import asd.project.domain.dto.ui.UIActiveStateDTO;
import asd.project.domain.dto.ui.UIDeathStateDTO;
import asd.project.domain.dto.ui.UIEndState;
import asd.project.domain.dto.ui.UIStartStateDTO;
import asd.project.domain.entity.Player;
import asd.project.state.ActiveState;
import asd.project.state.EndState;
import asd.project.state.StartState;
import java.awt.Color;

public class NetworkHelper extends Helper {

  public NetworkHelper(Game game) {
    super(game);
  }

  public void sendGameStateToUI(GameStateDTO gameStateDTO) {
    var world = game.getWorld();
    var player = world.getPlayer();
    var playerChunk = player.getChunk();

    if (gameStateDTO.health() <= 0) {
      handlePlayerDeath(gameStateDTO);
      return;
    }

    if (gameStateDTO.flagCount() >= 1) {
      handlePlayerWin(new WinGameDTO(gameStateDTO.playerUUID()));
      return;
    }

    if (!playerChunk.getUUID().equals(gameStateDTO.chunkUUID())) {
      return;
    }

    var playerHashMap = playerChunk.getPlayerHashMap();

    if (playerHashMap.containsKey(gameStateDTO.playerUUID())) {
      var targetPlayer = playerHashMap.get(gameStateDTO.playerUUID());

      targetPlayer.move(new Position(gameStateDTO.x(), gameStateDTO.y()));
    } else {
      playerChunk.addToPlayerHashMap(new Player(playerChunk,
          new Position(gameStateDTO.x(), gameStateDTO.y()), gameStateDTO.playerUUID(),
          player.getPower(), player.getHealth(), player.getMoney()));
    }

    if (!(game.getState() instanceof ActiveState)) {
      return;
    }

    game.getUI().setStateDTO(new UIActiveStateDTO(player));
  }

  public void sendMessageToUI(MessageDTO messageDTO) {
    game.getUI().updateMessage(new Message(messageDTO.getValue(), messageDTO.getColor()));
  }

  public void startGame(StartGameDTO startGameDTO) {
    var world = game.getWorld();
    world.initializeWorld(startGameDTO.worldSeed());
    Chunk chunk = world.expand(startGameDTO.chunkUUID());
    Player player = world.initializePlayer(chunk);
    game.getUI().setStatePainter(new UIActiveStateDTO(player));
    game.setState(new ActiveState(game));
  }

  public void pauseGame() {
    game.getUI().setStatePainter(new UIStartStateDTO());
    game.setState(new StartState(game));
  }

  public void disconnectClient() {
    game.getUI().updateMessage(new Message("The host has paused the game!", Color.BLUE));
  }

  protected void handlePlayerDeath(GameStateDTO gameStateDTO) {
    var player = game.getWorld().getPlayer();
    var playerChunk = player.getChunk();

    Player deathPlayer = new Player(gameStateDTO.playerUUID());
    game.getStorage().deletePlayer(deathPlayer);

    if (deathPlayer.getUUID().equals(player.getUUID())) {
      game.getUI().setStateDTO(new UIDeathStateDTO(player));
      game.getUI().setStatePainter(new UIDeathStateDTO(player));
      game.setState(new EndState(game));
    }

    playerChunk.getPlayerHashMap().remove(gameStateDTO.playerUUID());

    if (game.getStorage().getAmountOfKnightsAlive() <= 1
        && game.getState() instanceof ActiveState) {
      game.getNetwork().sendMessage(new MessageDTO(game.getUsername() + " won!", Color.GREEN));

      game.getUI().setStateDTO(new UIEndState(player, "WON"));
      game.getUI().setStatePainter(new UIEndState(player, "WON"));
      game.setState(new EndState(game));
    }
  }

  public void handlePlayerWin(WinGameDTO winGameDTO) {
    var player = game.getWorld().getPlayer();
    var playerChunk = player.getChunk();

    if (player.getUUID()
        .equals(playerChunk.getPlayerHashMap().get(winGameDTO.winnerUuid()).getUUID())) {
      game.getNetwork().sendMessage(new MessageDTO(game.getUsername() + " won!", Color.GREEN));
      game.getUI().setStateDTO(new UIEndState(player, "WON"));
      game.getUI().setStatePainter(new UIEndState(player, "WON"));
      game.setState(new EndState(game));
    } else {
      game.getUI().setStateDTO(new UIEndState(player, "LOST"));
      game.getUI().setStatePainter(new UIEndState(player, "LOST"));
      game.setState(new EndState(game));
    }
  }
}
