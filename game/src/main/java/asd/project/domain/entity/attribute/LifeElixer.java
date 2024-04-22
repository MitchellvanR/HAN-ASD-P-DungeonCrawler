package asd.project.domain.entity.attribute;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;
import asd.project.domain.entity.EntityConstants;

public class LifeElixer extends AttributeEntity {

  public LifeElixer(Chunk chunk, Position position) {
    super(CharacterConfiguration.HEALTH_ATTRIBUTE, chunk, position);
  }

  @Override
  public void publicInteract(CharacterEntity characterEntity) {
    characterEntity.setHealth(EntityConstants.PLAYER_MAX_HEALTH);
  }

  @Override
  public void privateInteract(CharacterEntity characterEntity) {

  }
}
