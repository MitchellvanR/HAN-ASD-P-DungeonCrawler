package asd.project.world.connections.msp;

import asd.project.world.connections.PositionConnection;
import asd.project.world.rooms.Room;
import java.util.List;

/**
 * Interface for a minimum spanning tree generator A minimum spanning tree is a tree that connects
 * all the rooms with the minimum possible number of connections The implementation of this
 * interface should be able to generate a minimum spanning tree between rooms The connections are
 * represented by {@link PositionConnection} The rooms are represented by {@link Room}
 */
public interface IMSPGenerator {

  /**
   * Generates a minimum spanning tree between the rooms
   *
   * @param roomList the list of rooms to connect
   * @return the list of connections between the rooms
   */
  List<PositionConnection> generateConnections(List<Room> roomList);
}

