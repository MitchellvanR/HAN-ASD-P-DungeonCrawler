package asd.project.domain.entity;

import asd.project.Game;
import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import java.util.UUID;

public class Player extends CharacterEntity {

  private boolean isBeingPlayed;
  private transient Game game;

  public Player(Chunk chunk) {
    super(CharacterConfiguration.PLAYER, chunk, new Position(0, 0), UUID.randomUUID(),
        EntityConstants.PLAYER_POWER, EntityConstants.PLAYER_MAX_HEALTH, 0);
  }

  public Player(Chunk chunk, Position position, UUID uuid, int power, int health, int money) {
    super(CharacterConfiguration.PLAYER, chunk, position, uuid, power, health, money);
  }

  public Player(Position position, UUID uuid, int power, int health, int money) {
    super(CharacterConfiguration.PLAYER, null, position, uuid, power, health, money);
  }

  public Player(UUID playerUUID) {
    super(CharacterConfiguration.PLAYER, null, new Position(0, 0), playerUUID,
        0, 0, 0);
  }

  public boolean getIsBeingPlayed() {
    return this.isBeingPlayed;
  }

  public void setIsBeingPlayed(boolean isBeingPlayed) {
    this.isBeingPlayed = isBeingPlayed;
  }

  public Game getGame() {
    return this.game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

}
