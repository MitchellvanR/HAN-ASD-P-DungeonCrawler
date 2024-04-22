package asd.project.transforms;

import asd.project.ast.AST;
import asd.project.ast.ASTNode;
import asd.project.ast.Behaviour;
import asd.project.ast.FoundBehaviours;
import asd.project.ast.Sentence;
import asd.project.ast.expression.IfElseExpression;
import asd.project.ast.expression.LogicalExpression;
import asd.project.ast.operation.NotOperation;
import java.util.ArrayList;
import java.util.List;

public class Evaluator implements Transform {

  @Override
  public void apply(AST ast) {
    FoundBehaviours foundBehaviours = ast.getRoot();

    evaluateFoundBehaviours(foundBehaviours);
  }

  private void evaluateFoundBehaviours(FoundBehaviours foundBehaviours) {
    for (ASTNode child : foundBehaviours.getChildren()) {
      if (child instanceof Sentence sentence) {
        evaluateSentence(sentence);
      }
    }
  }

  private void evaluateSentence(Sentence sentence) {
    List<ASTNode> nodesToKeep = new ArrayList<>();
    boolean isFirstBehaviourFound = false;

    for (ASTNode child : sentence.getChildren()) {
      if (child instanceof LogicalExpression logicalExpression) {
        List<ASTNode> logicalChildren = logicalExpression.getChildren();

        if (logicalChildren.size() >= 2 && !(logicalChildren.get(1) instanceof NotOperation)) {
          logicalChildren.remove(logicalChildren.size() - 1);
        }

        if (!isFirstBehaviourFound && logicalChildren.get(0) instanceof Behaviour &&
            !(logicalChildren.get(0) instanceof NotOperation)) {
          nodesToKeep.add(logicalExpression);
          isFirstBehaviourFound = true;
        }
      } else if (child instanceof IfElseExpression) {
        nodesToKeep.add(child);
      }
    }

    sentence.getChildren().clear();
    sentence.getChildren().addAll(nodesToKeep);

    if (!nodesToKeep.isEmpty() && isFirstBehaviourFound && nodesToKeep.get(
        0) instanceof LogicalExpression logicalExpression) {
      evaluateLogicalExpression(logicalExpression);
    } else if (!nodesToKeep.isEmpty() && !isFirstBehaviourFound && nodesToKeep.get(
        0) instanceof IfElseExpression ifElseExpression) {
      evaluateIfElseExpression(ifElseExpression);
    }
  }

  private void evaluateLogicalExpression(LogicalExpression logicalExpression) {
    // Evaluate logical expression if needed in the future
  }

  private void evaluateIfElseExpression(IfElseExpression ifElseExpression) {
    // Evaluate ifElse expression if needed in the future
  }
}
