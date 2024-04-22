package asd.project.domain.dto.ui;

import asd.project.domain.entity.Player;
import java.util.Objects;

public class UIDeathStateDTO extends UIConfigurationStateDTO {

  private final Player player;

  public UIDeathStateDTO(Player player) {
    super(UIState.DEATH_STATE);
    this.player = player;
    promptList.add("You have died.");
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
    UIDeathStateDTO that = (UIDeathStateDTO) object;
    return Objects.equals(player, that.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player);
  }
}
