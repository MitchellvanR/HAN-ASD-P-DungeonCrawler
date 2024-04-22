package asd.project.world.connections.msp;

import asd.project.domain.Position;
import java.util.HashMap;
import java.util.Map;

class Vertex {

  private final Map<Vertex, Edge> edges = new HashMap<>();
  private final Position position;
  private boolean isVisited = false;

  public Vertex(Position position) {
    this.position = position;
  }

  public EdgeVertexPair nextShortestEdge() {
    var nextMinimum = new Edge(Integer.MAX_VALUE, null);
    Vertex nextVertex = this;

    for (Map.Entry<Vertex, Edge> pair : edges.entrySet()) {
      if (pair.getKey().isVisited() && (pair.getValue().weight() < nextMinimum.weight())) {
        nextMinimum = pair.getValue();
        nextVertex = pair.getKey();

      }
    }

    return new EdgeVertexPair(nextMinimum, nextVertex);
  }

  public Position getPosition() {
    return position;
  }

  public boolean isVisited() {
    return !isVisited;
  }

  public void setVisited(boolean visited) {
    isVisited = visited;
  }

  public Map<Vertex, Edge> getEdges() {
    return edges;
  }
}

