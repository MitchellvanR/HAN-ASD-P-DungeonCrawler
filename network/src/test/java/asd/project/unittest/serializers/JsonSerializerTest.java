package asd.project.serializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.domain.Room;
import asd.project.dto.GameCodeFileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@DisplayName("Tests for JsonSerializer")
public class JsonSerializerTest {

  @Mock
  private JsonMapper mapperMock;

  @InjectMocks
  private JsonSerializer sut;

  @BeforeEach
  public void beforeEach() {
    mapperMock = mock(JsonMapper.class);
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("serialize1 object to json")
  void serialize1() throws JsonProcessingException {
    // Arrange
    Room room = new Room("127.0.0.1", 7777);
    String expectedJson = "{\"ip\":\"127.0.0.1\",\"port\":7777}";
    when(mapperMock.writeValueAsString(room)).thenReturn(expectedJson);

    // Act
    String actual = sut.serialize(room);

    // Assert
    assertEquals(expectedJson, actual);
    verify(mapperMock, times(1)).writeValueAsString(room);
  }

  @Test
  @DisplayName("serialize2 null object throws exception and returns null")
  void serialize2() throws JsonProcessingException {
    // Arrange
    when(mapperMock.writeValueAsString(null)).thenThrow(JsonProcessingException.class);

    // Act & Assert
    String actual = sut.serialize(null);

    // Assert
    assertNull(actual);
  }

  @Test
  @DisplayName("deserialize1 json to object")
  void deserialize1() throws IOException {
    // Arrange
    Class<GameCodeFileDto> gameCodeFileDtoType = GameCodeFileDto.class;
    String gameCodeFileDtoJson = "{\"port\":7777,\"ip\":\"127.0.0.1\"}";
    GameCodeFileDto expectedObject = new GameCodeFileDto(7777, "127.0.0.1");
    ObjectReader objectReaderMock = mock(ObjectReader.class);
    JavaType javaType = Mockito.any(JavaType.class);

    when(mapperMock.constructType(gameCodeFileDtoType)).thenReturn(javaType);
    when(mapperMock.readerFor(javaType)).thenReturn(objectReaderMock);
    when(objectReaderMock.readValue(gameCodeFileDtoJson)).thenReturn(expectedObject);

    // Act
    GameCodeFileDto actual = sut.deserialize(gameCodeFileDtoType, gameCodeFileDtoJson);

    // Assert
    assertEquals(expectedObject, actual);
    verify(objectReaderMock, times(1)).readValue(gameCodeFileDtoJson);
  }

  @Test
  @DisplayName("deserialize2 null json throws exception and returns null")
  void deserialize2() throws JsonProcessingException {
    // Arrange
    Class<GameCodeFileDto> gameCodeFileDtoType = GameCodeFileDto.class;
    ObjectReader objectReaderMock = mock(ObjectReader.class);
    JavaType javaType = Mockito.any(JavaType.class);

    when(mapperMock.constructType(gameCodeFileDtoType)).thenReturn(javaType);
    when(mapperMock.readerFor(javaType)).thenReturn(objectReaderMock);
    when(objectReaderMock.readValue((String) null)).thenThrow(JsonProcessingException.class);

    // Act & Assert
    GameCodeFileDto actual = sut.deserialize(gameCodeFileDtoType, null);

    // Assert
    assertNull(actual);
  }
}
