package asd.project.unittest.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.connection.DatabaseProperties;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for ConnectionCreator class")
class ConnectionCreatorTest {

  private ConnectionCreator sut;
  private Connection connectionMock;

  private DatabaseProperties databasePropertiesMock;

  @BeforeEach
  void beforeMethod() {
    this.connectionMock = mock(Connection.class);
    this.databasePropertiesMock = mock(DatabaseProperties.class);

    this.sut = new ConnectionCreator(databasePropertiesMock);
  }

  @Test
  @DisplayName("create1_check if it is a valid prepared statement")
  void create1() throws SQLException {
    /* Arrange */
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    when(connectionMock.prepareStatement("test")).thenReturn(preparedStatement);

    when(databasePropertiesMock.connect(anyString())).thenReturn(connectionMock);

    sut.connect("testDatabase");

    /* Act */
    PreparedStatement actual = sut.create("test");

    /* Assert */
    assertEquals(preparedStatement, actual);
  }

  @Test
  @DisplayName("create2_check if exception is thrown")
  void create2() throws SQLException {
    /* Arrange */
    when(connectionMock.prepareStatement("test")).thenThrow(RuntimeException.class);

    /* Act & Assert */
    assertThrows(RuntimeException.class, () -> {
      sut.create("test");
    });
  }
}
