package asd.project.sqlite.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseProperties {

  public static final String RESOURCES_FOLDER_PATH = "storage/src/main/resources/";
  private final Logger logger = Logger.getLogger(DatabaseProperties.class.getName());

  public Connection connect(String databaseName) {
    String url = "jdbc:sqlite:" + RESOURCES_FOLDER_PATH + databaseName;
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      logger.log(Level.SEVERE, String.format("Failed to connect to database %s", databaseName));
    }
    return conn;
  }
}
