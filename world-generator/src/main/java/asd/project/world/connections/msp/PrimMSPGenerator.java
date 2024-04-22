package asd.project.world.connections.msp;

import asd.project.world.connections.PositionConnection;
import asd.project.world.rooms.Room;
import java.util.ArrayList;
import java.util.List;

public class PrimMSPGenerator implements IMSPGenerator {

  @Override
  public List<PositionConnection> generateConnections(List<Room> roomList) {
    var connections = new ArrayList<PositionConnection>();
    var vertices = new ArrayList<Vertex>();

    roomList.forEach(room -> vertices.add(new Vertex(room.getRelativeCenterTile().getPosition())));

    createConnections(vertices);

    var graph = generateGraph(vertices);
    graph.forEach(pair -> connections.add(
        new PositionConnection(
            pair.vertex().getPosition(),
            pair.edge().endVertex().getPosition()
        ))
    );

    return connections;
  }

  private void createConnections(ArrayList<Vertex> vertices) {
    for (Vertex vertex : vertices) {
      for (Vertex otherVertex : vertices) {
        if (vertex != otherVertex && !otherVertex.getEdges().containsKey(vertex)) {
          var edge = new Edge(vertex.getPosition().distanceTo(otherVertex.getPosition()), vertex);
          vertex.getEdges().put(otherVertex, edge);
        }
      }
    }
  }

  private ArrayList<EdgeVertexPair> generateGraph(ArrayList<Vertex> vertices) {
    var graph = new ArrayList<EdgeVertexPair>();
    if (!vertices.isEmpty()) {
      vertices.get(0).setVisited(true);
    }

    while (isDisconnected(vertices)) {
      var nextMinimum = new Edge(Integer.MAX_VALUE, null);
      var nextVertex = vertices.get(0);

      for (Vertex vertex : vertices) {
        var candidate = vertex.nextShortestEdge();
        if (candidate.edge().weight() < nextMinimum.weight()) {
          nextMinimum = candidate.edge();
          nextVertex = candidate.vertex();
        }
      }

      nextVertex.setVisited(true);
      graph.add(new EdgeVertexPair(nextMinimum, nextVertex));
    }

    return graph;
  }

  private boolean isDisconnected(List<Vertex> graph) {
    for (Vertex vertex : graph) {
      if (vertex.isVisited()) {
        return true;
      }
    }
    return false;
  }
}

