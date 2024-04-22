package asd.project.ast;

import java.util.Objects;

public class ScalarLiteral extends ASTNode {

  private int value;

  public ScalarLiteral(int value) {
    this.value = value;
  }

  public ScalarLiteral(String text) {
    if (text.isBlank()) {
      return;
    }
    this.value = Integer.parseInt(text);
  }

  @Override
  public String getNodeLabel() {
    return "Scalar (" + value + ")";
  }

  public int getValue() {
    return value;
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
    ScalarLiteral that = (ScalarLiteral) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
