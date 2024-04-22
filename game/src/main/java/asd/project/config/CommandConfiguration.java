package asd.project.config;

public enum CommandConfiguration {
  CONFIGURE_AGENT("configure agent"),
  HOST("host"),
  JOIN_LOBBY("join"),
  MOVE_DOWN("move down"),
  MOVE_LEFT("move left"),
  MOVE_RIGHT("move right"),
  MOVE_UP("move up"),
  NEW_GAME("new"),
  REJOIN_GAME("rejoin"),
  START_GAME("start game"),
  LEAVE_GAME("leave"),
  ITEM_COUNT("item count"),
  DIFFICULTY("difficulty"),
  FLAG_COUNT("flag count"),
  PAUSE_GAME("pause game");

  private final String command;

  CommandConfiguration(String command) {
    this.command = command;
  }

  @Override
  public String toString() {
    return command;
  }
}
