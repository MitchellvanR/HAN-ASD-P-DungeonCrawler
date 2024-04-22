package asd.project.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import asd.project.Network;
import asd.project.domain.ClientSocket;
import asd.project.domain.dto.serializable.MessageDTO;
import asd.project.domain.dto.serializable.StartGameDTO;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

@DisplayName("Tests for Server")
class ServerTest {

  private Server sut;

  private MockedConstruction<Socket> socketMock;

  @BeforeEach
  public void beforeEach() {
    Network networkMock = mock(Network.class);
    sut = spy(new Server(networkMock));

    socketMock = mockConstruction(Socket.class);
  }

  @AfterEach
  public void afterEach() {
    socketMock.close();
  }

  @Test
  @DisplayName("handleGameStart1_check if game can't be started when there are no players")
  public void handleGameStart1() {
    /*Arrange*/
    var startGameDTOMock = mock(StartGameDTO.class);
    sut.setClientSocketList(spy(new ArrayList<>()));

    /* Act */
    sut.handleGameStart(startGameDTOMock);

    /* Assert */
    verify(sut).handleOutput(any());
    verify(sut.getClientSocketList(), never()).size();
  }

  @Test
  @DisplayName("handleGameStart2_check if game can't be started when there are too many players")
  public void handleGameStart2() {
    /*Arrange*/
    List<ClientSocket> socketMockList = new ArrayList<>();

    for (int i = 0; i < 26; i++) {
      var clientSocket = new ClientSocket();
      clientSocket.setClientUuid(UUID.randomUUID());
      clientSocket.setSocket(new Socket());
      socketMockList.add(clientSocket);
    }

    sut.setClientSocketList(socketMockList);
    var startGameDTO = mock(StartGameDTO.class);

    /* Act */
    sut.handleGameStart(startGameDTO);

    /* Assert */
    verify(sut).handleOutput(any(MessageDTO.class));
    verify(sut.getClientSocketList().get(0).getSocket(), never()).getInetAddress();
  }

  @Test
  @DisplayName("handleGameStart3_check if a sub-ip list and active state are sent")
  void handleGameStart3() {
    /* Arrange */
    List<ClientSocket> socketMockList = new ArrayList<>();

    var clientSocket = new ClientSocket();
    clientSocket.setClientUuid(UUID.randomUUID());
    clientSocket.setSocket(new Socket());

    socketMockList.add(clientSocket);

    sut.setClientSocketList(socketMockList);

    var startGameDTOMock = mock(StartGameDTO.class);
    InetAddress inetAdressMock = mock(InetAddress.class);
    when(socketMock.constructed().get(0).getInetAddress()).thenReturn(inetAdressMock);

    /* Act */
    sut.handleGameStart(startGameDTOMock);

    /* Assert */
    verify(sut).handleOutput(any(MessageDTO.class));
    verify(sut).handleOutput(any(StartGameDTO.class));
  }
}