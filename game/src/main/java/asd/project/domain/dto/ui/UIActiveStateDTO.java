package asd.project.domain.dto.ui;

import static asd.project.domain.dto.ui.UIState.ACTIVE_STATE;

import asd.project.domain.entity.Player;
import java.util.Objects;

public class UIActiveStateDTO extends UIStateDTO {

  private final Player player;

  public UIActiveStateDTO(Player player) {
    super(ACTIVE_STATE);
    this.player = player;
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
    UIActiveStateDTO that = (UIActiveStateDTO) object;
    return Objects.equals(player, that.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player);
  }
}
