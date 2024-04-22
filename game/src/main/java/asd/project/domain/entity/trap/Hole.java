package asd.project.domain.entity.trap;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.IInteractable;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;
import asd.project.domain.entity.Entity;

public class Hole extends Entity implements IInteractable {

  int power;

  public Hole(Chunk chunk, Position position, int power) {
    super(CharacterConfiguration.HOLE_TRAP, chunk, position);
    this.power = power;
  }

  @Override
  public void publicInteract(CharacterEntity characterEntity) {
    characterEntity.setHealth(characterEntity.getHealth() - power);
  }

  @Override
  public void privateInteract(CharacterEntity characterEntity) {

  }
}