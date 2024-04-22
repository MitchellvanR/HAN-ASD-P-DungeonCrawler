package asd.project.sqlite.dao.savegame;

import asd.project.domain.dto.storage.StorageWorldConfiguration;
import asd.project.domain.entity.Player;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.dao.PlayerDAO;
import asd.project.sqlite.util.StorageUtil;

public class SaveGame {

  private final ChunkDAO chunkDAO;
  private final PlayerDAO playerDAO;
  private final WorldPropertiesDAO worldPropertiesDAO;

  public SaveGame(ConnectionCreator connectionCreator) {
    chunkDAO = new ChunkDAO(connectionCreator);
    playerDAO = new PlayerDAO(connectionCreator);
    worldPropertiesDAO = new WorldPropertiesDAO(connectionCreator);
  }

  public void setDatabaseNames(String databaseName) {
    chunkDAO.setDatabaseName(databaseName);
    playerDAO.setDatabaseName(databaseName);
    worldPropertiesDAO.setDatabaseName(databaseName);
  }

  public void createFileOfSavedGame(String roomCode, Player player,
      StorageWorldConfiguration worldConfiguration) {
    String databaseName = roomCode + ".db";
    setDatabaseNames(databaseName);
    StorageUtil.createDatabaseIfNotExists(databaseName);
    createTables();
    addAllPlayersAndChunks(player);
    worldPropertiesDAO.addWorldProperty(worldConfiguration);
  }

  public void createTables() {
    chunkDAO.createChunkTable();
    playerDAO.createPlayerTable();
    worldPropertiesDAO.createWorldPropertyTable();
  }

  public void addAllPlayersAndChunks(Player player) {
    chunkDAO.addChunk(player.getChunk());
    player.getChunk().getPlayerHashMap()
        .forEach((uuid, playerObject) -> playerDAO.addPlayer(playerObject));
  }


}
