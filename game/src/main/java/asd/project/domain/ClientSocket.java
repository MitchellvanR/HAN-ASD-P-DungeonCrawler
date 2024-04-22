package asd.project.domain;

import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class ClientSocket {

  private UUID clientUuid;
  private Socket socket;

  private long lastMessageSent;

  public ClientSocket() {
    this.lastMessageSent = System.currentTimeMillis();
  }

  public long getLastMessageSent() {
    return lastMessageSent;
  }

  public void setLastMessageSent(long lastMessageSent) {
    this.lastMessageSent = lastMessageSent;
  }

  public UUID getClientUuid() {
    return clientUuid;
  }

  public void setClientUuid(UUID clientUuid) {
    this.clientUuid = clientUuid;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public InetAddress getInetAddress() {
    return socket.getInetAddress();
  }
}
