package asd.project.unittest.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import asd.project.sqlite.connection.DatabaseProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@DisplayName("Test for Database properties class")
class DatabasePropertiesTest {

  private DatabaseProperties sut;
  private MockedStatic<DriverManager> driverManagerMock;

  @BeforeEach
  void beforeMethod() {
    this.sut = new DatabaseProperties();
    driverManagerMock = Mockito.mockStatic(DriverManager.class);
  }

  @AfterEach
  void afterMethod() {
    driverManagerMock.close();
  }

  @Test
  @DisplayName("connect1_check if connection is valid")
  void connect1() throws SQLException {
    /* Arrange */
    Connection connection = mock(Connection.class);
    when(DriverManager.getConnection(
        "jdbc:sqlite:storage/src/main/resources/testDatabase")).thenReturn(connection);

    /* Act */
    Connection actual = sut.connect("testDatabase");

    /* Assert */
    assertEquals(connection, actual);
  }
}
