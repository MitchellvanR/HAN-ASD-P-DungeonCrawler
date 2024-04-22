package asd.project.ast.operation;

import asd.project.ast.LogicalOperator;

public class NotOperation extends LogicalOperator {

  @Override
  public String getNodeLabel() {
    return "NotOperator";
  }
}
