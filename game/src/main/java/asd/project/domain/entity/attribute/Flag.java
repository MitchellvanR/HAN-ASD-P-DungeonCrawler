package asd.project.domain.entity.attribute;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;

public class Flag extends AttributeEntity {

  public Flag(Chunk chunk, Position position) {
    super(CharacterConfiguration.FLAG_ATTRIBUTE, chunk, position);
  }

  @Override
  public void interact(CharacterEntity characterEntity) {
    super.interact(characterEntity);
    characterEntity.addFlag();
  }

  @Override
  public void publicInteract(CharacterEntity characterEntity) {
    characterEntity.addFlag();
  }

  @Override
  public void privateInteract(CharacterEntity characterEntity) {

  }
}
