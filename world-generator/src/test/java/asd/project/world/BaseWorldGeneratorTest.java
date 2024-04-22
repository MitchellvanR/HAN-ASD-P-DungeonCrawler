package asd.project.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import asd.project.config.world.WorldGeneratorConfiguration;
import asd.project.domain.Chunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for BaseWorldGenerator")
class BaseWorldGeneratorTest {

  TestWorldGenerator sut;

  @BeforeEach
  void beforeEach() {
    var configuration = new WorldGeneratorConfiguration(10, 10, 10);
    sut = new TestWorldGenerator();
    sut.setWorldConfiguration(configuration);
  }

  @Test
  @DisplayName("getWorldConfiguration1_WhenWorldConfigurationIsRetrieved_ThenWorldConfigurationIsCorrect")
  void getWorldConfiguration1() {
    // Arrange
    var configuration = new WorldGeneratorConfiguration(10, 10, 10);

    // Act
    var result = sut.getWorldConfiguration();

    // Assert
    assertEquals(configuration, result);
  }

  @Test
  @DisplayName("getWorldConfiguration2_WhenWorldConfigurationIsGivenTwice_ThenErrorIsThrown")
  void getWorldConfiguration2() {
    // Arrange
    var configuration = new WorldGeneratorConfiguration(10, 10, 10);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> sut.setWorldConfiguration(configuration));
  }

  class TestWorldGenerator extends BaseWorldGenerator {

    public TestWorldGenerator() {
    }

    @Override
    protected Chunk internalGenerateChunk(WorldGenerationConfiguration configuration) {
      return null;
    }

    @Override
    protected boolean isConfigurationValid(WorldGenerationConfiguration configuration) {
      return false;
    }
  }
}