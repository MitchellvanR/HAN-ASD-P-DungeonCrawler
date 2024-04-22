package asd.project.domain.entity.trap;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.domain.Chunk;
import asd.project.domain.World;
import asd.project.domain.entity.Player;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for Hole")
class HoleTest {

  Hole sut;

  @BeforeEach
  void beforeEach() {
    sut = new Hole(null, null, 0);
  }

  @Test
  @DisplayName("interact1_Test if health is altered")
  void interact1() {
    // Arrange
    var mockedPlayer = mock(Player.class);

    var mockedChunk = mock(Chunk.class);
    var mockedWorld = mock(World.class);

    when(mockedPlayer.getChunk()).thenReturn(mockedChunk);
    when(mockedChunk.getWorld()).thenReturn(mockedWorld);
    when(mockedWorld.getPlayer()).thenReturn(mockedPlayer);
    doReturn(UUID.randomUUID()).when(mockedPlayer).getUUID();
    // Act
    sut.interact(mockedPlayer);

    // Assert
    verify(mockedPlayer).setHealth(anyInt());
  }
}