package asd.project.ast;

import java.util.ArrayList;

public class Condition extends ASTNode {

  private AgentFeature feature;
  private ComparisonOperator comparisonOperator;
  private ScalarLiteral scalarLiteral;

  @Override
  public String getNodeLabel() {
    return "Condition";
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    if (child instanceof AgentFeature agentFeature) {
      feature = agentFeature;
    } else if (child instanceof ComparisonOperator comparison) {
      this.comparisonOperator = comparison;
    } else if (child instanceof ScalarLiteral newScalarLiteral) {
      this.scalarLiteral = newScalarLiteral;
    }

    return this;
  }

  @Override
  public ArrayList<ASTNode> getChildren() {
    ArrayList<ASTNode> children = new ArrayList<>();
    children.add(feature);
    children.add(comparisonOperator);
    children.add(scalarLiteral);

    return children;
  }
}
