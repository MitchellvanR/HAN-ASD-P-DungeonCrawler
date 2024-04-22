package asd.project.domain.entity.attribute;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;

public class Saw extends AttributeEntity {

  public Saw(Chunk chunk, Position position) {
    super(CharacterConfiguration.SAW_ATTRIBUTE, chunk, position);
  }

  @Override
  public void publicInteract(CharacterEntity characterEntity) {
    characterEntity.setPowerRate(characterEntity.getPowerRate() + 1);
  }

  @Override
  public void privateInteract(CharacterEntity characterEntity) {

  }
}
