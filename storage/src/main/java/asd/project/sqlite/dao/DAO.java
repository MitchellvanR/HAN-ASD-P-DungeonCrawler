package asd.project.sqlite.dao;

import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.exceptions.CouldNotConvertMultipleResultsetsException;
import asd.project.sqlite.exceptions.CouldNotCreatePreparedStatementException;
import asd.project.sqlite.exceptions.CouldNotExecuteQueryException;
import asd.project.sqlite.exceptions.CouldNotRetrieveDataException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Super class for the DAO. This class encapsulates the most important methods to make a database
 * connection and write/read to or from the database.
 *
 * @param <E> The type of {@link Object} as return value for the converting
 */
public abstract class DAO<E> {

  public static final String DATABASE_NAME = "gamestate.db";
  private static final String FAILED_TO_RECEIVE_DATA = "Failed to receive data from ";
  private final Logger logger = Logger.getLogger(DAO.class.getName());
  private final ConnectionCreator connectionCreator;
  private String databaseName;

  protected DAO(ConnectionCreator connectionCreator) {
    this.connectionCreator = connectionCreator;
  }

  protected PreparedStatement connect(String sql, String databaseName) {
    this.connectionCreator.connect(databaseName);
    return this.connectionCreator.create(sql);
  }

  protected void setString(PreparedStatement statement, String value, int index) {
    try {
      statement.setString(index, value);
    } catch (Exception e) {
      throw new CouldNotCreatePreparedStatementException(
          String.format("Failed to set string. %n %s %n At index: %s.", value, index));
    }
  }

  protected void setInt(PreparedStatement statement, int value, int index) {
    try {
      statement.setInt(index, value);
    } catch (Exception e) {
      throw new CouldNotCreatePreparedStatementException(
          String.format("Failed to set Integer. %n %s %n At index: %s.", value, index));
    }
  }

  public int getInt(ResultSet set, String column) {
    try {
      return set.getInt(column);
    } catch (SQLException e) {
      throw new CouldNotRetrieveDataException(
          String.format("%s %s with datatype Integer", FAILED_TO_RECEIVE_DATA, column));
    }
  }

  public String getString(ResultSet set, String column) {
    try {
      return set.getString(column);
    } catch (Exception e) {
      throw new CouldNotRetrieveDataException(
          String.format("%s %s with datatype String", FAILED_TO_RECEIVE_DATA, column));
    }
  }

  public void execute(PreparedStatement statement) {
    try {
      statement.execute();
    } catch (Exception exception) {
      throw new CouldNotExecuteQueryException(
          String.format("Failed to execute query: %s.", exception));
    } finally {
      try {
        statement.close();
      } catch (Exception exception) {
        logger.log(Level.INFO, String.format("Failed to close statement: %s", exception));
      }
    }
  }

  public ResultSet executeQuery(PreparedStatement statement) {
    try {
      return statement.executeQuery();
    } catch (Exception exception) {
      throw new CouldNotExecuteQueryException(
          String.format("Failed to execute query: %s", exception));
    }
  }

  protected List<E> convertMultiple(ResultSet set) {
    List<E> results = new ArrayList<>();

    try {
      while (set.next()) {
        results.add(convert(set));
      }
      set.close();
    } catch (Exception exception) {
      throw new CouldNotConvertMultipleResultsetsException(
          String.format("Failed to execute multiple conversions: %s", exception));
    }

    return results;
  }

  /**
   * An abstract method to convert a {@link ResultSet} to {@link Object} DTO So it converts a
   * ResultSet to given {@link Object} in the class generics.
   *
   * @param set The {@link ResultSet} to be converted
   * @return The given {@link Object} in the class generic
   */
  protected abstract E convert(ResultSet set);

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }
}
