package asd.project.sqlite.connection;

import asd.project.sqlite.exceptions.CouldNotCreatePreparedStatementException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionCreator {

  private final Logger logger = Logger.getLogger(ConnectionCreator.class.getName());
  private final DatabaseProperties databaseProperties;
  private Connection connection;

  public ConnectionCreator(DatabaseProperties databaseProperties) {
    this.databaseProperties = databaseProperties;
  }

  public void connect(String databaseName) {
    try {
      this.connection = databaseProperties.connect(databaseName);
    } catch (Exception exception) {
      logger.log(Level.SEVERE,
          String.format("Failed to find database at %s . %s", databaseName, exception));
    }
  }

  public PreparedStatement create(String sql) {
    try {
      return connection.prepareStatement(sql);
    } catch (Exception exception) {
      throw new CouldNotCreatePreparedStatementException(
          String.format("Failed to create a query to the database. %s", exception));
    }
  }
}
