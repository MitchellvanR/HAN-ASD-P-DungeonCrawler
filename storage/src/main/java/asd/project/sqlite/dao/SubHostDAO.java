package asd.project.sqlite.dao;

import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.sqlite.util.StorageUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubHostDAO extends DAO<SubHostListDTO> {

  public SubHostDAO(ConnectionCreator connectionCreator) {
    super(connectionCreator);
    onInit();
  }

  private void onInit() {
    setDatabaseName(DAO.DATABASE_NAME);
    StorageUtil.createDatabaseIfNotExists(getDatabaseName());
    dropSubHostListTable();
    createSubHostListTable();
    deleteAllSubHostsFromTable();
  }

  private void createSubHostListTable() {
    String sql = """
        CREATE TABLE IF NOT EXISTS subHostList (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        gameCode VARCHAR(30),
        ip VARCHAR(30)
        );""";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public void dropSubHostListTable() {
    String sql = "DROP TABLE IF EXISTS subhostlist;";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public void deleteAllSubHostsFromTable() {
    String sql = "DELETE FROM subHostList";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public void deleteSubHost(SubHostDTO subHostDTO) {
    String sql = "DELETE FROM subHostList where ip = ?";

    PreparedStatement statement = connect(sql, getDatabaseName());
    setString(statement, subHostDTO.subHost(), 1);

    execute(statement);
  }

  public void addSubHost(SubHostDTO subHostDTO) {
    String sql = "INSERT INTO subHostList (gameCode, ip) VALUES (?, ?)";
    PreparedStatement statement = connect(sql, getDatabaseName());

    setString(statement, subHostDTO.gameCode(), 1);
    setString(statement, subHostDTO.subHost(), 2);
    execute(statement);
  }

  public SubHostListDTO getSubHostList() {
    String sql = "SELECT gameCode, ip FROM subHostList";

    PreparedStatement statement = connect(sql, getDatabaseName());

    ResultSet resultSet = executeQuery(statement);
    return convert(resultSet);
  }


  @Override
  protected SubHostListDTO convert(ResultSet set) {
    SubHostListDTO subHosts = new SubHostListDTO(new ArrayList<>());
    try {
      while (set.next()) {
        subHosts.subHostDTOS().add(new SubHostDTO(set.getString("ip"), set.getString("gameCode")));
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    return subHosts;
  }
}
