package asd.project.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import asd.project.domain.Chunk;
import asd.project.domain.entity.Player;
import asd.project.sqlite.SQLiteStorage;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration-test")
@DisplayName("Integration-test-SQLiteStorage")
class SQLiteStorageIT {

  private SQLiteStorage sut;

  @BeforeEach
  void beforeEach() {
    sut = new SQLiteStorage();
  }

  @Test
  @DisplayName("getPlayerState1_test if player is saved correctly")
  void getPlayerState1() {
    //Arrange
    var chunk = new Chunk(10, 10, 0, UUID.randomUUID());
    var player = new Player(chunk);

    //Act
    sut.savePlayerState(player);
    var playerState = sut.getPlayerState(player.getUUID());

    //Assert
    assertEquals(player, playerState);
  }
}
