package asd.project.ast.expression;

import asd.project.ast.ASTNode;
import asd.project.ast.Condition;
import asd.project.ast.Expression;
import asd.project.ast.Sentence;
import java.util.ArrayList;

public class IfElseExpression extends Expression {

  private ASTNode condition;
  private ASTNode ifSentence;
  private ASTNode elseSentence;

  public IfElseExpression() {
    super();
  }

  @Override
  public String getNodeLabel() {
    return "IfElseExpression";
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    if (child instanceof Sentence) {
      if (ifSentence == null) {
        ifSentence = child;
      } else {
        elseSentence = child;
      }
    } else if (child instanceof Condition) {
      condition = child;
    }

    return this;
  }

  @Override
  public ArrayList<ASTNode> getChildren() {
    ArrayList<ASTNode> children = new ArrayList<>();
    children.add(condition);
    children.add(ifSentence);

    if (elseSentence != null) {
      children.add(elseSentence);
    }

    return children;
  }
}
