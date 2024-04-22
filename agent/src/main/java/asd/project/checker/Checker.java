package asd.project.checker;

import asd.project.ast.AST;
import asd.project.ast.ASTNode;
import asd.project.ast.AgentFeature;
import asd.project.ast.Behaviour;
import asd.project.ast.ComparisonOperator;
import asd.project.ast.Condition;
import asd.project.ast.FoundBehaviours;
import asd.project.ast.ScalarLiteral;
import asd.project.ast.Sentence;
import asd.project.ast.agentfeature.Health;
import asd.project.ast.agentfeature.Money;
import asd.project.ast.comparison.GreaterThan;
import asd.project.ast.comparison.LessThan;
import asd.project.ast.expression.IfElseExpression;
import asd.project.ast.expression.LogicalExpression;
import asd.project.ast.operation.NotOperation;
import java.util.ArrayList;

public class Checker {

  public void check(AST ast) {
    checkFoundBehaviours(ast.getRoot());
  }

  private void checkFoundBehaviours(ASTNode astNode) {
    FoundBehaviours foundBehaviours = (FoundBehaviours) astNode;
    for (ASTNode child : foundBehaviours.getChildren()) {
      if (child instanceof Sentence) {
        checkSentence(child);
      }
    }
  }

  private void checkSentence(ASTNode astNode) {
    Sentence sentence = (Sentence) astNode;

    checkValidSentence(sentence);

  }

  private void checkValidSentence(ASTNode sentence) {
    boolean behaviourIsPositive = false;

    for (ASTNode child : sentence.getChildren()) {
      if (child instanceof LogicalExpression expression) {
        checkLogicalExpression(child);

        if (expression.getOperator() instanceof NotOperation) {
          behaviourIsPositive = true;
        }
      } else if (child instanceof IfElseExpression ifElseExpression) {
        checkIfElseExpression(child);

        behaviourIsPositive = true;

        for (ASTNode ifElseNode : ifElseExpression.getChildren()) {
          if (ifElseNode instanceof Sentence) {
            checkValidSentence(ifElseNode);
          }
        }
      }
    }

    if (!behaviourIsPositive) {
      sentence.setError("Een zin moet minimaal één positieve waarde voor een gedrag hebben.");
    }
  }

  private void checkLogicalExpression(ASTNode logicalExpression) {
    ArrayList<ASTNode> children = logicalExpression.getChildren();
    boolean noBehaviour = true;

    for (ASTNode child : children) {
      if (child instanceof Behaviour) {
        noBehaviour = false;
        break;
      }
    }

    if (noBehaviour) {
      logicalExpression.setError(
          "Een logische expressie moet altijd minimaal één Behaviour bevatten");
    }
  }

  private void checkIfElseExpression(ASTNode ifElseExpression) {
    ArrayList<ASTNode> children = ifElseExpression.getChildren();

    for (ASTNode child : children) {
      if (child instanceof Condition) {
        checkConditionExpression(child);
      }
    }
  }

  private void checkConditionExpression(ASTNode conditionExpression) {
    ArrayList<ASTNode> children = conditionExpression.getChildren();

    for (ASTNode child : children) {
      if (child instanceof AgentFeature) {
        checkAgentFeature(child);
      } else if (child instanceof ComparisonOperator) {
        checkComparisonOperator(child, conditionExpression);
      } else if (child instanceof ScalarLiteral) {
        checkScalarLiteral(child);
      }
    }
  }

  private void checkScalarLiteral(ASTNode scalarLiteral) {
    ScalarLiteral scalar = (ScalarLiteral) scalarLiteral;
    if (scalar.getValue() < 0) {
      scalarLiteral.setError("Getal kan niet negatief zijn.");
    }
  }

  private void checkComparisonOperator(ASTNode comparisonOperator, ASTNode conditionalExpression) {
    if (!(comparisonOperator instanceof LessThan) && !(comparisonOperator instanceof GreaterThan)) {
      conditionalExpression.setError(
          "Comparison operator maakt geen gebruik van " + LessThan.class.getSimpleName() + " of "
              + GreaterThan.class.getSimpleName() + ".");
    }
  }

  private void checkAgentFeature(ASTNode agentFeature) {
    if (!(agentFeature instanceof Health) && !(agentFeature instanceof Money)) {
      agentFeature.setError(
          "Agent feature is verkeerd ingevuld. Kies uit: " + Health.class.getSimpleName() + ", "
              + Money.class.getSimpleName() + ".");
    }
  }


}