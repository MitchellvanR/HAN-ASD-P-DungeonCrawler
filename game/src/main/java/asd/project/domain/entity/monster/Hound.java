package asd.project.domain.entity.monster;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.CharacterEntity;
import java.util.UUID;

public class Hound extends CharacterEntity {

  public Hound(Chunk chunk, Position position, UUID uuid, int power, int health, int money) {
    super(CharacterConfiguration.HOUND_ENTITY, chunk, position, uuid, power, health, money);
  }
}
