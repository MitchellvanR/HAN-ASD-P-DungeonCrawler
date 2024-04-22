package asd.project.helper;

import asd.project.Game;
import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.domain.dto.storage.StorageGameStateDTO;
import asd.project.domain.dto.storage.StorageWorldConfiguration;
import asd.project.domain.entity.Player;

public class StorageHelper extends Helper {

  public StorageHelper(Game game) {
    super(game);
  }

  public void sendPlayerStateToDatabase(Player player) {
    this.game.getStorage().savePlayerState(player);
  }

  public void sendSubHostToDatabase(SubHostListDTO subHostListDTO) {
    this.game.getStorage().saveSubHost(subHostListDTO);
  }

  public void deleteSubHost(SubHostDTO subHostDTO) {
    this.game.getStorage().deleteSubHost(subHostDTO);
  }

  public void setSubHostList() {
    SubHostListDTO subHostListDTO = this.game.getStorage().getSubHostList();
    this.game.getNetwork().setSubHostList(subHostListDTO);
  }

  public void deleteAllSubhosts() {
    this.game.getStorage().deleteAllSubhosts();
  }

  public void sendGameStateToDatabase() {
    StorageGameStateDTO storageGameStateDTO = new StorageGameStateDTO(
        this.game.getWorld().getPlayer());

    StorageWorldConfiguration storageWorldConfiguration = new StorageWorldConfiguration(10, "easy");
    this.game.getStorage().sendGameStateToDatabase(storageGameStateDTO, storageWorldConfiguration,
        this.game.getRoomCode());
  }
}