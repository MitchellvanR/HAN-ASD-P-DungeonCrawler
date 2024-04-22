package asd.project.config.world;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityConfig implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(EntityConfig.class.getName());

  private static final int DEFAULT_POWER = 25;
  private static final int DEFAULT_HEALTH = 25;
  private static final int DEFAULT_N_O_FLAGS = 0;
  private static final int MAX_N_O_FLAGS = 5;
  private static final String FILE_PATH = "game/src/main/resources/entityConfig.json";
  private int power;
  private int health;
  private String difficulty = "normal";
  private int itemCount = 20;
  private int flagCount;
  private int maxMonsterCount;
  private int minMonsterCount;

  public EntityConfig() {
    this.power = DEFAULT_POWER;
    this.health = DEFAULT_HEALTH;
    this.flagCount = DEFAULT_N_O_FLAGS;
  }

  public EntityConfig(int minMonsterCount, int maxMonsterCount, int flagCount) {
    this.maxMonsterCount = maxMonsterCount;
    this.minMonsterCount = minMonsterCount;
    this.flagCount = flagCount;
    this.power = DEFAULT_POWER;
    this.health = DEFAULT_HEALTH;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
    if (difficulty.equals("easy")) {
      power = (int) Math.round(DEFAULT_POWER * 0.75);
      health = (int) Math.round(DEFAULT_HEALTH * 0.75);
    } else if (difficulty.equals("hard")) {
      power = (int) Math.round(DEFAULT_POWER * 1.25);
      health = (int) Math.round(DEFAULT_HEALTH * 1.25);
    }
  }

  public int getItemCount() {
    return itemCount;
  }

  public void setItemCount(int itemCount) {
    int maxItemCount = 50;
    int minItemCount = 10;
    if (itemCount > maxItemCount) {
      this.itemCount = maxItemCount;
    } else if (itemCount < minItemCount) {
      this.itemCount = minItemCount;
    } else {
      this.itemCount = itemCount;
    }
  }

  public void setConfig(Object entityConfig) {
    Gson gson = new Gson();
    FileWriter fileWriter = null;
    createFileIfNotExists();
    try {
      fileWriter = new FileWriter(FILE_PATH);
      gson.toJson(entityConfig, fileWriter);
      fileWriter.close();
    } catch (IOException e) {
      e.fillInStackTrace();
    }
  }

  public EntityConfig readEntityConfig() {
    EntityConfig entityConfig = null;
    Gson gson = new Gson();
    createFileIfNotExists();
    try {
      Reader reader = Files.newBufferedReader(
          Paths.get(FILE_PATH));
      entityConfig = gson.fromJson(reader, EntityConfig.class);
      reader.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
    }
    return entityConfig;
  }

  private void createFileIfNotExists() {
    try {
      File file = new File(FILE_PATH);
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING,
          String.format("An error occurred while creating the file: %s.", FILE_PATH));
    }
  }

  public int getPower() {
    return power;
  }

  public int getHealth() {
    return health;
  }

  public int getMaxMonsterCount() {
    return maxMonsterCount;
  }

  public int getMinMonsterCount() {
    return minMonsterCount;
  }

  public int getFlagCount() {
    return flagCount;
  }

  public void setFlagCount(int flagCount) {
    if (flagCount > MAX_N_O_FLAGS) {
      flagCount = MAX_N_O_FLAGS;
    }
    this.flagCount = flagCount;
  }
}
