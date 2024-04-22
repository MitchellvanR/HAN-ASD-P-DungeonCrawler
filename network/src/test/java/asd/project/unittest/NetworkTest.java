package asd.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.domain.Room;
import asd.project.exceptions.CreateRoomException;
import asd.project.exceptions.InvalidRoomCodeException;
import asd.project.server.Server;
import asd.project.services.IDropboxService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

@DisplayName("Tests for Network")
class NetworkTest {

  @Mock
  private IDropboxService dropboxServiceMock;

  @InjectMocks
  private Network sut;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("createRoom1_Test getting roomCode and call on bootstrapping service")
  public void createRoom1() throws UnknownHostException, CreateRoomException {
    // Arrange
    Room testRoom = new Room(InetAddress.getLocalHost().getHostAddress(), 7777);
    when(dropboxServiceMock.uploadRoom(any(Room.class), eq(true))).thenReturn("ABCDE");
    when(dropboxServiceMock.isCanConnect()).thenReturn(true);

    // Act
    String roomCode = sut.createRoom(7777);

    // Assert
    assertEquals("ABCDE", roomCode);
    verify(dropboxServiceMock).uploadRoom(testRoom, true);
  }

  @Test
  @DisplayName("createRoom2_Test creating room with invalid IP address")
  public void createRoom2() throws CreateRoomException {
    // Arrange
    MockedStatic<InetAddress> inetAddressMock = mockStatic(InetAddress.class);
    inetAddressMock.when(InetAddress::getLocalHost).thenThrow(UnknownHostException.class);

    // Act
    String roomCode = sut.createRoom();

    // Assert
    assertNull(roomCode);
  }

  @Test
  @DisplayName("joinGame1_test if getGameCodeFileDto throws an InvalidRoomCodeException if an invalid room code is filled in")
  void joinGame1() {
    // Arrange
    when(dropboxServiceMock.isCanConnect()).thenReturn(true);

    // Act & Assert
    assertThrows(InvalidRoomCodeException.class, () -> sut.joinGame(null));
  }
}
