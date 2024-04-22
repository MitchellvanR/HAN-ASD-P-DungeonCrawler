package asd.project.config;

public enum CharacterConfiguration {
  PLAYER('P'),
  WALL_TILE('#'),
  OPEN_TILE(' '),
  SAW_ATTRIBUTE('S'),
  SKELETON_ENTITY('@'),
  SKELETONWARRIOR_ENTITY('ψ'),
  LICH_ENTITY('L'),
  WOLF_ENTITY('w'),
  HOUND_ENTITY('H'),
  HELLHOUND_ENTITY('B'),
  DEMONEYE('•'),
  DEMONKNIGHT('‡'),
  DRAGON_ENTITY('D'),
  HEALTH_ATTRIBUTE('H'),
  DOUBLE_COINS_ATTRIBUTE('€'),
  FLAG_ATTRIBUTE('F'),
  EMPTY_TILE(' '),
  HOLE_TRAP('.');

  private final char character;

  CharacterConfiguration(char character) {
    this.character = character;
  }

  @Override
  public String toString() {
    return String.valueOf(character);
  }
}
