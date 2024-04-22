package asd.project.sqlite;

import asd.project.IStorage;
import asd.project.sqlite.dao.PlayerDAO;
import asd.project.sqlite.dao.SubHostDAO;
import asd.project.domain.dto.storage.StorageGameStateDTO;
import asd.project.domain.dto.storage.StorageWorldConfiguration;
import asd.project.domain.entity.Player;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.connection.DatabaseProperties;
import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.sqlite.dao.savegame.SaveGame;
import java.util.UUID;

public class SQLiteStorage implements IStorage {

  private DatabaseProperties databaseProperties;
  private ConnectionCreator connectionCreator;
  private SubHostDAO subHostDAO;
  private final PlayerDAO playerDAO;
  private final SaveGame saveGame;

  public SQLiteStorage() {
    databaseProperties = new DatabaseProperties();
    connectionCreator = new ConnectionCreator(databaseProperties);
    playerDAO = new PlayerDAO(connectionCreator);
    subHostDAO = new SubHostDAO(connectionCreator);
    saveGame = new SaveGame(connectionCreator);
  }

  @Override
  public void savePlayerState(Player player) {
    if (!playerDAO.getAllPlayers().isEmpty()) {
      playerDAO.updateAllPlayersInChunk(player);
    } else {
      playerDAO.addPlayer(player);
    }
  }

  public void saveSubHost(SubHostListDTO subHostListDTO) {
    for (SubHostDTO subHostDTO : subHostListDTO.subHostDTOS()) {
      subHostDAO.addSubHost(subHostDTO);
    }
  }

  @Override
  public void deleteSubHost(SubHostDTO subHostDTO) {
    subHostDAO.deleteSubHost(subHostDTO);
  }

  @Override
  public void deleteAllSubhosts() {
    subHostDAO.deleteAllSubHostsFromTable();
  }

  @Override
  public SubHostListDTO getSubHostList() {
    return subHostDAO.getSubHostList();
  }

  @Override
  public Player getPlayerState(Player player) {
    return playerDAO.getPlayer(player.getUUID().toString());
  }

  @Override
  public Player getPlayerState(UUID uuid) {
    return playerDAO.getPlayer(uuid.toString());
  }

  public void sendGameStateToDatabase(StorageGameStateDTO gameStateObject,
      StorageWorldConfiguration worldConfiguration, String roomCode) {
    saveGame.createFileOfSavedGame(roomCode, gameStateObject.player(), worldConfiguration);
  }

  @Override
  public void deletePlayer(Player player) {
    playerDAO.deletePlayer(player);
  }

  @Override
  public int getAmountOfKnightsAlive() {
    return playerDAO.getAllPlayers().size();
  }

}
