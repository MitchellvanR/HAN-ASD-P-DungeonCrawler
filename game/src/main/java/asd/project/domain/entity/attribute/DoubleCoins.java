package asd.project.domain.entity.attribute;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.IInteractable;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;

public class DoubleCoins extends AttributeEntity implements IInteractable {

  public DoubleCoins(Chunk chunk, Position position) {
    super(CharacterConfiguration.DOUBLE_COINS_ATTRIBUTE, chunk, position);
  }

  @Override
  public void publicInteract(CharacterEntity characterEntity) {
    characterEntity.setMoneyRate(characterEntity.getMoneyRate() + 1);
  }

  @Override
  public void privateInteract(CharacterEntity characterEntity) {

  }
}
