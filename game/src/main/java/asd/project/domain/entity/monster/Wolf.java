package asd.project.domain.entity.monster;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;
import java.util.UUID;

public class Wolf extends CharacterEntity {

  public Wolf(Chunk chunk, Position position, UUID uuid, int power, int health, int money) {
    super(CharacterConfiguration.WOLF_ENTITY, chunk, position, uuid, power, health, money);
  }
}
