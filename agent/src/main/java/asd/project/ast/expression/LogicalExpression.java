package asd.project.ast.expression;

import asd.project.ast.ASTNode;
import asd.project.ast.Behaviour;
import asd.project.ast.Expression;
import asd.project.ast.LogicalOperator;
import java.util.ArrayList;

public class LogicalExpression extends Expression {

  private LogicalOperator operator;
  private Behaviour behaviour;

  public LogicalExpression() {
    super();
  }

  @Override
  public String getNodeLabel() {
    return "LogicalExpression";
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    if (child instanceof LogicalOperator logicalOperator) {
      operator = logicalOperator;
    } else if (child instanceof Behaviour newBehaviour) {
      behaviour = newBehaviour;
    }

    return this;
  }

  @Override
  public ArrayList<ASTNode> getChildren() {
    ArrayList<ASTNode> children = new ArrayList<>();
    if (operator != null) {
      children.add(operator);
    }
    children.add(behaviour);

    return children;
  }

  public LogicalOperator getOperator() {
    return operator;
  }

  public Behaviour getBehaviour() {
    return behaviour;
  }
}
