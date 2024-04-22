package asd.project.unittest.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import asd.project.domain.Chunk;
import asd.project.domain.Position;
import asd.project.domain.entity.Player;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.connection.DatabaseProperties;
import asd.project.sqlite.dao.PlayerDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@DisplayName("Test for PlayerDAO class")
class PlayerDAOTest {

  private PlayerDAO sut;
  private PreparedStatement prepared_statementMock;
  private MockedStatic<DriverManager> driverManagerMock;
  private Connection connectionMock;
  private ResultSet setMock;

  @BeforeEach
  void beforeMethod() {
    ConnectionCreator connection_creatorMock = mock(ConnectionCreator.class);
    DatabaseProperties databasePropertiesMock = mock(DatabaseProperties.class);

    this.prepared_statementMock = mock(PreparedStatement.class);
    this.connectionMock = mock(Connection.class);
    this.driverManagerMock = Mockito.mockStatic(DriverManager.class);
    this.setMock = mock(ResultSet.class);

    when(databasePropertiesMock.connect(anyString())).thenReturn(connectionMock);
    when(connection_creatorMock.create(anyString())).thenReturn(prepared_statementMock);

    this.sut = new PlayerDAO(connection_creatorMock);
  }

  @AfterEach
  void afterMethod() {
    driverManagerMock.close();
  }

  @Test
  @DisplayName("getAllPlayers1_check if you get all players")
  void getAllPlayers1() throws SQLException {
    /* Arrange */
    when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);

    int power = 5;
    int health = 100;
    int money = 10;

    ResultSet mockedSet = ResultSetCreator.create(
        new String[]{"playerUUID", "x", "y", "chunkUUID", "power", "health", "money"},
        new Object[][]{
            {"e58ed763-928c-4155-bee9-fdbaaadc15f3", 0, 1, "43e53222-89cd-48c4-be8c-ec32d2f7d994",
                power, health, money},
            {"ffd46642-f0b7-49f2-bd53-6607834c0a12", 10, 10,
                "9d41283b-3bb2-4eff-b04c-cf124f79efb0", power, health, money},
            {"cc75f721-d2d7-4c15-aacd-5f5ddc846c97", 20, 30,
                "bc67575e-8924-4c60-832b-d82b6e1ac1ba", power, health, money},
            {"b55bff29-03b0-47a6-abcf-ce0caca77155", 40, 40, "2ecabfe5-7e9a-47dd-862b-3827fb8a3619",
                power, health, money}
        }
    );

    when(prepared_statementMock.executeQuery()).thenReturn(mockedSet);

    Player player1 = new Player(
        new Chunk(10, 10, 10, UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3")),
        new Position(0, 1), UUID.fromString("43e53222-89cd-48c4-be8c-ec32d2f7d994"), power, health,
        money);
    Player player2 = new Player(
        new Chunk(10, 10, 10, UUID.fromString("ffd46642-f0b7-49f2-bd53-6607834c0a12")),
        new Position(10, 10), UUID.fromString("9d41283b-3bb2-4eff-b04c-cf124f79efb0"), power,
        health, money);
    Player player3 = new Player(
        new Chunk(10, 10, 10, UUID.fromString("cc75f721-d2d7-4c15-aacd-5f5ddc846c97")),
        new Position(20, 30), UUID.fromString("bc67575e-8924-4c60-832b-d82b6e1ac1ba"), power,
        health, money);
    Player player4 = new Player(
        new Chunk(10, 10, 10, UUID.fromString("b55bff29-03b0-47a6-abcf-ce0caca77155")),
        new Position(40, 40), UUID.fromString("2ecabfe5-7e9a-47dd-862b-3827fb8a3619"), power,
        health, money);

    List<Player> expected = new ArrayList<>();
    expected.add(player1);
    expected.add(player2);
    expected.add(player3);
    expected.add(player4);

    /* Act */
    List<Player> actual = sut.getAllPlayers();

    /* Assert */
    assertEquals(expected.size(), actual.size());
  }

  @Test
  @DisplayName("dropPlayerTable1_check if all players are deleted")
  void dropPlayerTable1() {
    /* Arrange */

    /* Act */

    /* Assert */
    assertDoesNotThrow(() -> sut.dropPlayerTable());
  }

  @Test
  @DisplayName("deletePlayer1_check if a player gets deleted")
  void deletePlayer1() {
    /* Arrange */
    Player p = new Player(new Chunk(10, 10, 10, UUID.
        fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3")), new Position(0, 1),
        UUID.fromString("43e53222-89cd-48c4-be8c-ec32d2f7d994"), 5, 100, 10);

    /* Act */

    /* Assert */
    assertDoesNotThrow(() -> sut.deletePlayer(p));
  }

  @Test
  @DisplayName("updatePlayer1_check if player gets updated")
  void updatePlayer1() {
    /* Arrange */
    Player p = new Player(new Chunk(10, 10, 10, UUID.
        fromString("b55bff29-03b0-47a6-abcf-ce0caca77155")), new Position(40, 40),
        UUID.fromString("2ecabfe5-7e9a-47dd-862b-3827fb8a3619"), 5, 100, 10);

    /* Act */

    /* Assert */
    assertDoesNotThrow(() -> sut.updatePlayer(p));
  }

  @Test
  @DisplayName("addPlayer1_check if a player gets added")
  void addPlayer1() {
    /* Arrange */
    Player p = new Player(new Chunk(10, 10, 10, UUID.
        fromString("cc75f721-d2d7-4c15-aacd-5f5ddc846c97")), new Position(20, 30),
        UUID.fromString("bc67575e-8924-4c60-832b-d82b6e1ac1ba"), 5, 100, 10);

    /* Act */

    /* Assert */
    assertDoesNotThrow(() -> sut.addPlayer(p));
  }

  @Test
  @DisplayName("getPlayer1_check if there is a player in the database")
  void getPlayer1() throws SQLException {
    /* Arrange */
    when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);
    String playerUUID = "cc75f721-d2d7-4c15-aacd-5f5ddc846c97";
    String chunkUUID = "bc67575e-8924-4c60-832b-d82b6e1ac1ba";
    int x = 20;
    int y = 30;
    int power = 5;
    int health = 100;
    int money = 10;
    ResultSet mockedSet = ResultSetCreator.create(
        new String[]{"playerUUID", "x", "y", "chunkUUID", "power", "health", "money"},
        new Object[][]{
            {playerUUID, x, y, chunkUUID, power, health, money}
        }
    );

    mockedSet.next();

    when(prepared_statementMock.executeQuery()).thenReturn(mockedSet);

    when(setMock.next()).thenReturn(true);
    when(setMock.getString("playerUUID")).thenReturn(playerUUID);
    when(setMock.getInt("x")).thenReturn(x);
    when(setMock.getInt("y")).thenReturn(y);

    Player expected = new Player(new Position(x, y), UUID.fromString(playerUUID), power, health,
        money);

    /* Act */
    Player actual = sut.getPlayer("cc75f721-d2d7-4c15-aacd-5f5ddc846c97");

    /* Assert */
    assertEquals(expected.getUUID(), actual.getUUID());
  }

  @Test
  @DisplayName("updateAllPlayersInChunk1_check if players gets updated in a given chunk")
  void updateAllPlayersInChunk1() throws SQLException {
    /* Arrange */
    ResultSet mockedSet = ResultSetCreator.create(
        new String[]{"playerUUID", "x", "y", "chunkUUID", "power", "health", "money"},
        new Object[][]{
            {"e58ed763-928c-4155-bee9-fdbaaadc15f3", 0, 1, "43e53222-89cd-48c4-be8c-ec32d2f7d994",
                5, 100, 10},
            {"ffd46642-f0b7-49f2-bd53-6607834c0a12", 10, 10,
                "9d41283b-3bb2-4eff-b04c-cf124f79efb0", 5, 100, 10},
            {"cc75f721-d2d7-4c15-aacd-5f5ddc846c97", 20, 30,
                "bc67575e-8924-4c60-832b-d82b6e1ac1ba", 5, 100, 10},
            {"b55bff29-03b0-47a6-abcf-ce0caca77155", 40, 40, "2ecabfe5-7e9a-47dd-862b-3827fb8a3619",
                5, 100, 10}
        }
    );

    when(prepared_statementMock.executeQuery()).thenReturn(mockedSet);

    Player p = new Player(
        new Chunk(10, 10, 10, UUID.fromString("cc75f721-d2d7-4c15-aacd-5f5ddc846c97")),
        new Position(20, 30),
        UUID.fromString("bc67575e-8924-4c60-832b-d82b6e1ac1ba"), 5, 100, 10);

    /* Act */
    assertDoesNotThrow(() -> sut.updateAllPlayersInChunk(p));
  }
}
