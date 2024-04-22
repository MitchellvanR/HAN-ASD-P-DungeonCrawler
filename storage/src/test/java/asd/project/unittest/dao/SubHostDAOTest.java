package asd.project.unittest.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import asd.project.domain.dto.SubHostDTO;
import asd.project.domain.dto.SubHostListDTO;
import asd.project.sqlite.connection.ConnectionCreator;
import asd.project.sqlite.connection.DatabaseProperties;
import asd.project.sqlite.dao.SubHostDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@DisplayName("Test for SubHostDAO class")
public class SubHostDAOTest {

    private SubHostDAO sut;
    private PreparedStatement prepared_statementMock;
    private MockedStatic<DriverManager> driverManagerMock;
    private Connection connectionMock;


    @BeforeEach
    void beforeMethod() {
        ConnectionCreator connection_creatorMock = mock(ConnectionCreator.class);
        DatabaseProperties databasePropertiesMock = mock(DatabaseProperties.class);

        this.prepared_statementMock = mock(PreparedStatement.class);
        this.connectionMock = mock(Connection.class);
        this.driverManagerMock = Mockito.mockStatic(DriverManager.class);

        when(databasePropertiesMock.connect(anyString())).thenReturn(connectionMock);
        when(connection_creatorMock.create(anyString())).thenReturn(prepared_statementMock);

        this.sut = spy(new SubHostDAO(connection_creatorMock));

    }

    @AfterEach
    void afterMethod() {
        driverManagerMock.close();
    }

    @Test
    @DisplayName("deleteAllSubHostsFromTable1_check if delete all query is executed")
    public void deleteAllSubHostsFromTable1() throws SQLException {
        /* Arrange */
        when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);

        /* Act */
        sut.deleteAllSubHostsFromTable();

        /* Assert */
        //on init doet 3 executes dus de vierde execute is van deleteAllSubHostsFromTable
        Mockito.verify(prepared_statementMock, Mockito.times(4)).execute();
    }

    @Test
    @DisplayName("deleteSubHost1_check if delete query is executed")
    public void deleteSubHost1() throws SQLException {
        /* Arrange */
        when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);

        SubHostDTO subHostDTO = new SubHostDTO("ip1", "gameCode1");

        /* Act */
        sut.deleteSubHost(subHostDTO);

        /* Assert */
        //on init doet 3 executes dus de vierde execute is van deleteSubHost
        Mockito.verify(prepared_statementMock, Mockito.times(4)).execute();
    }

    @Test
    @DisplayName("addSubHost1_check if add subHost query is executed")
    public void addSubHost1() throws SQLException {
        /* Arrange */
        when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);

        SubHostDTO subHostDTO = new SubHostDTO("ip1", "gameCode1");

        /* Act */
        sut.addSubHost(subHostDTO);

        /* Assert */
        //on init doet 3 executes dus de vierde execute is van addSubHost
        Mockito.verify(prepared_statementMock, Mockito.times(4)).execute();
    }

    @Test
    @DisplayName("getSubHostList1_check if subHostList is returned")
    public void getSubHostList1() throws SQLException {
        /* Arrange */
        when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);

        ResultSet mockedSet = ResultSetCreator.create(
                new String[]{"ip", "gameCode"},
                new Object[][]{
                        {"ip1", "gameCode1"},
                        {"ip2", "gameCode1"},
                        {"ip3", "gameCode1"}
                }
        );

        when(prepared_statementMock.executeQuery()).thenReturn(mockedSet);


        SubHostDTO subHostDTO = new SubHostDTO("ip1", "gameCode1");
        SubHostDTO subHostDTO2 = new SubHostDTO("ip2", "gameCode1");
        SubHostDTO subHostDTO3 = new SubHostDTO("ip3", "gameCode1");

        SubHostListDTO expected = new SubHostListDTO(new ArrayList<>());
        expected.subHostDTOS().add(subHostDTO);
        expected.subHostDTOS().add(subHostDTO2);
        expected.subHostDTOS().add(subHostDTO3);

        /* Act */
        SubHostListDTO actual = sut.getSubHostList();

        /* Assert */
        assertEquals(expected.subHostDTOS().size(), actual.subHostDTOS().size());
    }

    @Test
    @DisplayName("getSubHostList2_check if subHostList is returned incorrectly")
    public void getSubHostList2() throws SQLException {
        /* Arrange */
        when(DriverManager.getConnection(anyString())).thenReturn(connectionMock);

        ResultSet mockedSet = ResultSetCreator.create(
                new String[]{"ip", "gameCode"},
                new Object[][]{
                        {"ip1", "gameCode1"},
                        {"ip2", "gameCode1"},
                        {"ip3", "gameCode1"},
                        {"ip4", "gameCode1"}
                }
        );

        when(prepared_statementMock.executeQuery()).thenReturn(mockedSet);


        SubHostDTO subHostDTO = new SubHostDTO("ip1", "gameCode1");
        SubHostDTO subHostDTO2 = new SubHostDTO("ip2", "gameCode1");
        SubHostDTO subHostDTO3 = new SubHostDTO("ip3", "gameCode1");

        SubHostListDTO expected = new SubHostListDTO(new ArrayList<>());
        expected.subHostDTOS().add(subHostDTO);
        expected.subHostDTOS().add(subHostDTO2);
        expected.subHostDTOS().add(subHostDTO3);

        /* Act */
        SubHostListDTO actual = sut.getSubHostList();

        /* Assert */
        assertNotEquals(expected.subHostDTOS().size(), actual.subHostDTOS().size());
    }

}
