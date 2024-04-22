package asd.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.domain.Room;
import asd.project.exceptions.HttpRequestException;
import asd.project.exceptions.HttpUnauthorizedException;
import asd.project.serializers.ISerializer;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@DisplayName("Tests for DropboxService")
class DropboxServiceTest {

  @Mock
  private ISerializer serializerMock;

  @InjectMocks
  private DropboxService sut;

  private Room room;

  private String roomJson;

  private String roomCode;

  @BeforeEach
  public void beforeEach() throws HttpUnauthorizedException {
    serializerMock = mock(ISerializer.class);
    MockitoAnnotations.openMocks(this);
    sut = Mockito.spy(sut);

    room = new Room("1", 1);
    roomJson = "{\"ip\":\"1\",\"port\":1}";
    roomCode = "roomCode";

    doReturn(false).when(sut).doesGameExist(any());
  }

  @Test
  @DisplayName("uploadRoom1_Room gets successfully uploaded to Dropbox")
  void uploadRoom1() throws HttpUnauthorizedException {
    // Arrange
    boolean retry = true;
    String expected = roomCode;
    when(serializerMock.serialize(room)).thenReturn(roomJson);
    when(sut.generateAndReturnRoomCode()).thenReturn(roomCode);

    // Act
    String actual = sut.uploadRoom(room, retry);

    // Assert
    verify(sut).uploadRoom(room, retry);

    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("uploadRoom2_Authentication called on unauthorized exception and retry = true")
  void uploadRoom2() throws HttpUnauthorizedException {
    // Arrange
    when(sut.generateAndReturnRoomCode()).thenThrow(HttpUnauthorizedException.class);

    // Act
    sut.uploadRoom(room, true);

    // Assert
    verify(sut).getAccessToken();
  }

  @Test
  @DisplayName("uploadRoom3_Authentication not called on unauthorized exception and retry = false")
  void uploadRoom3() throws HttpUnauthorizedException {
    // Arrange
    when(sut.generateAndReturnRoomCode()).thenThrow(HttpUnauthorizedException.class);

    // Act
    String actual = sut.uploadRoom(room, false);

    // Assert
    verify(sut, never()).getAccessToken();
    verify(sut, never()).getAccessToken();

    assertNull(actual);
  }

  @Test
  @SuppressWarnings("unchecked")
  @DisplayName("uploadRoom4_Authentication not called on unauthorized exception and retry = false")
  void uploadRoom4() throws HttpUnauthorizedException, HttpRequestException {
    // Arrange
    when(serializerMock.serialize(room)).thenReturn(roomJson);
    when(sut.handleResponse(any(HttpResponse.class))).thenThrow(HttpRequestException.class);
    when(sut.generateAndReturnRoomCode()).thenReturn(null);

    // Act
    String actual = sut.uploadRoom(room, true);

    // Assert
    assertNull(actual);
  }
}
