package asd.project.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import asd.project.Game;
import asd.project.INetwork;
import asd.project.IUI;
import asd.project.config.world.EntityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for monster configuration command")
public class ChangeMonsterDifficultyCommandTest {

  private ChangeMonsterDifficultyCommand sut;

  private Game gameMock;

  private EntityConfig configMock;

  private IUI uiMock;

  private INetwork networkMock;

  @BeforeEach
  void beforeEach() {
    gameMock = mock(Game.class);
    configMock = mock(EntityConfig.class);
    uiMock = mock(IUI.class);
    networkMock = mock(INetwork.class);
    when(gameMock.getUI()).thenReturn(uiMock);
    when(gameMock.getNetwork()).thenReturn(networkMock);
    sut = new ChangeMonsterDifficultyCommand(gameMock);
    sut.setEntityConfig(configMock);
  }

  @Test
  @DisplayName("setDifficultyEasy1_check if monster difficulty is set to easy")
  void setDifficultyEasy1() {
    //arrange
    String expectedDifficulty = "easy";
    String argument = "easy";
    when(configMock.readEntityConfig()).thenReturn(new EntityConfig(10, 50, 0));
    EntityConfig entityConfig = null;

    //act
    sut.perform(argument);
    entityConfig = configMock.readEntityConfig();

    //assert
    assertEquals(expectedDifficulty, entityConfig.getDifficulty());
  }

  @Test
  @DisplayName("setDifficultyNormal1_check if monster difficulty is set to normal")
  void setDifficultyNormal1() {
    //arrange
    String expectedDifficulty = "normal";
    String argument = "normal";
    when(configMock.readEntityConfig()).thenReturn(new EntityConfig(10, 50, 0));
    EntityConfig entityConfig = null;

    //act
    sut.perform(argument);
    entityConfig = configMock.readEntityConfig();

    //assert
    assertEquals(expectedDifficulty, entityConfig.getDifficulty());
  }

  @Test
  @DisplayName("setDifficultyHard1_check if monster difficulty is set to hard")
  void setDifficultyHard1() {
    //arrange
    String expectedDifficulty = "hard";
    String argument = "hard";
    when(configMock.readEntityConfig()).thenReturn(new EntityConfig(10, 50, 0));
    EntityConfig entityConfig = null;

    //act
    sut.perform(argument);
    entityConfig = configMock.readEntityConfig();

    //assert
    assertEquals(expectedDifficulty, entityConfig.getDifficulty());
  }
}
