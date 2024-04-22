package asd.project.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

@DisplayName("Tests for item count configuration command")
public class ChangeItemAmountCommandTest {

  private ChangeItemAmountCommand sut;

  private INetwork netwerkMock;

  private Game gameMock;

  private EntityConfig configMock;

  private IUI uiMock;

  @BeforeEach
  void beforeEach() {
    gameMock = mock(Game.class);
    configMock = mock(EntityConfig.class);
    uiMock = mock(IUI.class);
    netwerkMock = mock(INetwork.class);
    when(gameMock.getUI()).thenReturn(uiMock);
    when(gameMock.getNetwork()).thenReturn(netwerkMock);
    sut = new ChangeItemAmountCommand(gameMock);
    sut.setEntityConfig(configMock);
  }

  @Test
  @DisplayName("setItemCount1_check set itemCount to 35 when int value is given as string")
  void setItemCount1() {
    //arrange
    int expectedItemCount = 35;
    String argument = "35";
    configMock.setItemCount(Integer.parseInt(argument));
    when(configMock.readEntityConfig()).thenReturn(new EntityConfig(10, 50, 0));
    EntityConfig entityConfig;

    //act
    sut.perform(argument);
    entityConfig = configMock.readEntityConfig();

    //assert
    assertEquals(expectedItemCount, entityConfig.getItemCount());
  }

  @Test
  @DisplayName("setItemCount2_check if error is given when string value is given")
  void setItemCount2() {
    //arrange
    String argument = "hoi_hugo";
    when(configMock.readEntityConfig()).thenReturn(new EntityConfig(10, 50, 0));

    //assert
    assertDoesNotThrow(() -> sut.perform(argument));
  }
}
