package asd.project.sqlite.dao.savegame;

import asd.project.domain.dto.storage.StorageWorldConfiguration;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.dao.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WorldPropertiesDAO extends DAO<StorageWorldConfiguration> {

  public WorldPropertiesDAO(ConnectionCreator connectionCreator) {
    super(connectionCreator);
  }

  public void createWorldPropertyTable() {
    String sql = """
        CREATE TABLE IF NOT EXISTS world_property (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        entity_count int  NOT NULL,
        difficulty VARCHAR(20) NOT NULL
        );""";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public void addWorldProperty(StorageWorldConfiguration storageWorldConfiguration) {
    String sql = "INSERT INTO world_property (entity_count, difficulty) VALUES (?, ?)";

    PreparedStatement statement = connect(sql, getDatabaseName());
    setInt(statement, storageWorldConfiguration.entityCount(), 1);
    setString(statement, storageWorldConfiguration.difficulty(), 2);

    execute(statement);
  }

  @Override
  protected StorageWorldConfiguration convert(ResultSet set) {
    int entityCount = getInt(set, "entity_count");
    String difficulty = getString(set, "difficulty");

    return new StorageWorldConfiguration(entityCount, difficulty);
  }

}
