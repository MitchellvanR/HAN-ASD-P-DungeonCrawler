package asd.project.sqlite.util;

import asd.project.sqlite.connection.DatabaseProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageUtil {

  private static final Logger LOGGER = Logger.getLogger(StorageUtil.class.getName());

  private StorageUtil() {
    // Private constructor to prevent instantiation
  }

  public static void createDatabaseIfNotExists(String databaseName) {
    createResourcesPathIfNotExists();
    String url = "jdbc:sqlite:" + DatabaseProperties.RESOURCES_FOLDER_PATH + databaseName;

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn != null) {
        conn.getMetaData();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, () -> "Failed to create a database: " + e);
    }
  }

  public static void createResourcesPathIfNotExists() {
    Path resourcesPath = Paths.get(DatabaseProperties.RESOURCES_FOLDER_PATH);
    try {
      if (!Files.exists(resourcesPath)) {
        Files.createDirectories(resourcesPath);
      } else {
        LOGGER.log(Level.INFO, "Resources folder already exists.");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, () -> "Failed to create the resource folder: " + e);
    }
  }
}
