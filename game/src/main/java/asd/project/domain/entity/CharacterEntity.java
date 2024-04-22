package asd.project.domain.entity;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.IInteractable;
import asd.project.domain.Position;
import asd.project.domain.entity.attribute.AttributeEntity;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for all entities that have power, health and money Power is the strength a
 * character has Health is the live total, when health reaches zero. The entity dies. Money is the
 * amount the entity is worth.
 */
public abstract class CharacterEntity extends Entity implements IInteractable, Serializable {

  static Logger logger = Logger.getLogger(CharacterEntity.class.getName());

  protected int power;
  protected int health;
  protected int money;
  protected int moneyRate;
  protected int powerRate;
  protected int flagCount = 0;

  protected CharacterEntity(CharacterConfiguration characterConfiguration, Chunk chunk,
      Position position, UUID uuid, int power, int health, int money) {
    super(characterConfiguration, chunk, position, uuid);
    this.power = power;
    this.health = health;
    this.money = money;
    this.moneyRate = 1;
    this.powerRate = 1;

  }

  public void move(Position position) {
    if (!this.getChunk().getGrid().isPositionInGrid(position)) {
      return;
    }

    var targetTile = this.getChunk().getGrid().getTile(position);
    var currentTile = this.getChunk().getGrid().getTile(this.getPosition());

    if (targetTile.isWalkable()) {
      return;
    }

    if (targetTile.getEntity().isPresent()) {
      var targetEntity = targetTile.getEntity().get();
      if (targetEntity instanceof CharacterEntity characterEntity) {
        characterEntity.interact(this);
        return;
      }

      if (targetEntity instanceof AttributeEntity attributeEntity) {
        attributeEntity.interact(this);
      }

      if (targetEntity instanceof IInteractable interactable) {
        interactable.interact(this);
      }
    }

    currentTile.setEntity(null);
    targetTile.setEntity(this);
    this.setPosition(position);
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getPower() {
    return power;
  }

  public int getMoney() {
    return money;
  }

  public void setMoney(int money) {
    this.money = money;
  }

  public int getMoneyRate() {
    return moneyRate;
  }

  public void setMoneyRate(int moneyRate) {
    this.moneyRate = moneyRate;
  }

  public int getPowerRate() {
    return powerRate;
  }

  public void setPowerRate(int powerRate) {
    this.powerRate = powerRate;
  }

  @Override
  public void privateInteract(CharacterEntity characterEntity) {

  }

  @Override
  public void publicInteract(CharacterEntity characterEntity) {
    this.health -= (characterEntity.getPower() * characterEntity.getPowerRate());
    logger.log(Level.INFO, () -> String.format("%s", this.health));
    if (this.health <= 0) {
      this.getChunk().getGrid().getTile(this.getPosition()).setEntity(null);
      characterEntity.setMoney(
          characterEntity.getMoney() + (this.getMoney() * characterEntity.getMoneyRate()));
      this.setMoney(0);
      this.setHealth(0);
      this.setPowerRate(1);
      this.setMoneyRate(1);
      logger.log(Level.INFO, "Character dead!");
    }
  }

  public void addFlag() {
    this.flagCount++;
  }

  public int getFlagCount() {
    return flagCount;
  }
}
