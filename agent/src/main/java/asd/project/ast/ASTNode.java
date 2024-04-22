package asd.project.ast;

import asd.project.checker.SemanticError;
import java.util.ArrayList;
import java.util.Objects;

public class ASTNode {

  private SemanticError error = null;

  public String getNodeLabel() {
    return "ASTNode";
  }

  public ArrayList<ASTNode> getChildren() {
    return new ArrayList<>();
  }

  public ASTNode addChild(ASTNode child) {
    return this;
  }

  public ASTNode removeChild(ASTNode child) {
    return this;
  }

  public SemanticError getError() {
    return this.error;
  }

  public void setError(String description) {
    this.error = new SemanticError(description);
  }

  public boolean hasError() {
    return error != null;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    toString(result);
    return result.toString();
  }

  private void toString(StringBuilder builder) {
    builder.append("[");
    builder.append(getNodeLabel());
    builder.append("|");
    for (ASTNode child : getChildren()) {
      if (child != null) {
        child.toString(builder);
      }
    }
    builder.append("]");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ASTNode astNode = (ASTNode) o;
    return Objects.equals(error, astNode.error) && Objects.equals(getChildren(),
        ((ASTNode) o).getChildren());
  }

  @Override
  public int hashCode() {
    return Objects.hash(error);
  }
}
