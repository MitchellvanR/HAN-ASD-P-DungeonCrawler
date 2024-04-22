package asd.project.sqlite.dao.savegame;

import asd.project.domain.Chunk;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.dao.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChunkDAO extends DAO<Chunk> {

  public ChunkDAO(ConnectionCreator connectionCreator) {
    super(connectionCreator);
  }

  public void createChunkTable() {
    String sql = """
        CREATE TABLE IF NOT EXISTS chunk (
        chunkUUID VARCHAR(36) PRIMARY KEY NOT NULL,
        seed int NOT NULL
        );""";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public void addChunk(Chunk chunk) {
    String sql = "INSERT INTO chunk (chunkUUID, seed) VALUES (?, ?)";
    PreparedStatement statement = connect(sql, getDatabaseName());
    setString(statement, chunk.getUUID().toString(), 1);
    setInt(statement, 70, 2);
    execute(statement);
  }

  @Override
  protected Chunk convert(ResultSet set) {
    return null;
  }

}
