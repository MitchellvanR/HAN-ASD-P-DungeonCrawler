package asd.project.domain.dto.ui;

import asd.project.domain.entity.Player;
import java.util.Objects;

public class UIEndState extends UIConfigurationStateDTO {

  private final Player player;

  public UIEndState(Player player, String text) {
    super(UIState.END_STATE);
    this.player = player;
    promptList.add("YOU " + text + "!");
    promptList.add("You had " + player.getHealth() + " health left.");
    promptList.add("You had " + player.getMoney() + " money.");
  }

  public Player getPlayer() {
    return player;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UIEndState that = (UIEndState) object;
    return Objects.equals(player, that.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player);
  }
}
