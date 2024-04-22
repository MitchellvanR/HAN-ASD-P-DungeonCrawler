package asd.project.domain.entity.monster;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;
import java.util.UUID;

public class DemonKnight extends CharacterEntity {

  public DemonKnight(Chunk chunk, Position position, UUID uuid, int power, int health, int money) {
    super(CharacterConfiguration.DEMONKNIGHT, chunk, position, uuid, power, health, money);
  }

}
