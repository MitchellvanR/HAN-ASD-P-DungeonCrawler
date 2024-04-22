package asd.project.ast;

import java.util.ArrayList;
import java.util.Objects;

public class FoundBehaviours extends ASTNode {

  private final ArrayList<ASTNode> body;

  public FoundBehaviours() {
    this.body = new ArrayList<>();
  }

  public FoundBehaviours(ArrayList<ASTNode> body) {
    this.body = body;
  }

  @Override
  public String getNodeLabel() {
    return "Foundbehaviours";
  }

  @Override
  public ArrayList<ASTNode> getChildren() {
    return this.body;
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    body.add(child);
    return this;
  }

  @Override
  public ASTNode removeChild(ASTNode child) {
    body.remove(child);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    FoundBehaviours that = (FoundBehaviours) o;
    return Objects.equals(body, that.body);
  }

  @Override
  public int hashCode() {

    return Objects.hash(body);
  }
}
