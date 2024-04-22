package asd.project.sqlite.dao;

import asd.project.domain.Position;
import asd.project.domain.entity.Player;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.exceptions.CouldNotRetrieveDataException;
import asd.project.sqlite.util.StorageUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayerDAO extends DAO<Player> {

  public PlayerDAO(ConnectionCreator connectionCreator) {
    super(connectionCreator);
    onInit();
  }

  public void onInit() {
    setDatabaseName(DAO.DATABASE_NAME);
    StorageUtil.createDatabaseIfNotExists(getDatabaseName());
    dropPlayerTable();
    createPlayerTable();
  }

  public List<Player> getAllPlayers() {
    String sql = "SELECT playerUUID, x, y, chunkUUID, power, health, money FROM player";

    PreparedStatement statement = connect(sql, getDatabaseName());
    ResultSet resultSet = executeQuery(statement);

    return convertMultiple(resultSet);
  }

  public void addPlayer(Player player) {
    String sql =
        "INSERT INTO player (playerUUID, x, y, chunkUUID, power, health, money) VALUES (?, ?, ?, ?, ?, ?, ?)";

    PreparedStatement statement = connect(sql, getDatabaseName());
    setString(statement, player.getUUID().toString(), 1);
    setInt(statement, player.getPosition().getX(), 2);
    setInt(statement, player.getPosition().getY(), 3);
    setString(statement, player.getChunk().getUUID().toString(), 4);
    setInt(statement, player.getPower(), 5);
    setInt(statement, player.getHealth(), 6);
    setInt(statement, player.getMoney(), 7);

    execute(statement);
  }

  public void updatePlayer(Player player) {
    String sql =
        "UPDATE player SET x = ?, y = ?, chunkUUID = ?, power = ?, health = ?, money = ? WHERE playerUUID = ?";

    PreparedStatement statement = connect(sql, getDatabaseName());
    setInt(statement, player.getPosition().getX(), 1);
    setInt(statement, player.getPosition().getY(), 2);
    setString(statement, player.getChunk().getUUID().toString(), 3);
    setInt(statement, player.getPower(), 4);
    setInt(statement, player.getHealth(), 5);
    setInt(statement, player.getMoney(), 6);
    setString(statement, player.getUUID().toString(), 7);

    execute(statement);
  }

  public void dropPlayerTable() {
    String sql = "DROP TABLE IF EXISTS player;";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public void deletePlayer(Player player) {
    String sql = "DELETE FROM player where playerUUID = ?";

    PreparedStatement statement = connect(sql, getDatabaseName());
    setString(statement, player.getUUID().toString(), 1);

    execute(statement);
  }

  public void createPlayerTable() {
    String sql = """
        CREATE TABLE IF NOT EXISTS player (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        playerUUID VARCHAR(36) NOT NULL,
        x int NOT NULL,
        y int NOT NULL,
        chunkUUID VARCHAR(36) NOT NULL,
        power int NOT NULL,
        health int NOT NULL,
        money int NOT NULL
        );""";

    PreparedStatement statement = connect(sql, getDatabaseName());
    execute(statement);
  }

  public Player getPlayer(String playerUUID) {
    String sql =
        "SELECT playerUUID, x, y, chunkUUID, power, health, money FROM player WHERE playerUUID = ?";

    PreparedStatement statement = connect(sql, getDatabaseName());
    setString(statement, playerUUID, 1);
    ResultSet resultSet = executeQuery(statement);

    Player player = convert(resultSet);

    try {
      resultSet.close();
    } catch (SQLException exception) {
      throw new CouldNotRetrieveDataException(
          String.format("Player with UUID: %s, could not be retrieved. %s", playerUUID, exception));
    }

    return player;
  }

  public void updateAllPlayersInChunk(Player player) {
    List<Player> currentPlayers = getAllPlayers();

    player.getChunk().getPlayerHashMap().forEach((uuid, playerObject) -> {
      if (currentPlayers.stream().filter(c -> c.getUUID().toString().equals(uuid.toString()))
          .findFirst().isEmpty()) {
        if (playerObject.getHealth() != 0) {
          addPlayer(playerObject);
        }
      } else {
        updatePlayer(playerObject);
      }
    });
  }

  @Override
  protected Player convert(ResultSet set) {
    UUID playerUUID = UUID.fromString(getString(set, "playerUUID"));
    int x = getInt(set, "x");
    int y = getInt(set, "y");
    int power = getInt(set, "power");
    int health = getInt(set, "health");
    int money = getInt(set, "money");

    return new Player(new Position(x, y), playerUUID, power, health, money);
  }
}
