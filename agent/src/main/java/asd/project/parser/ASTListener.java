package asd.project.parser;

import asd.project.ast.AST;
import asd.project.ast.ASTNode;
import asd.project.ast.Behaviour;
import asd.project.ast.Condition;
import asd.project.ast.FoundBehaviours;
import asd.project.ast.ScalarLiteral;
import asd.project.ast.Sentence;
import asd.project.ast.agentfeature.Health;
import asd.project.ast.agentfeature.Money;
import asd.project.ast.behaviour.AggressiveBehaviour;
import asd.project.ast.behaviour.DefensiveBehaviour;
import asd.project.ast.behaviour.PassiveBehaviour;
import asd.project.ast.comparison.GreaterThan;
import asd.project.ast.comparison.LessThan;
import asd.project.ast.expression.IfElseExpression;
import asd.project.ast.expression.LogicalExpression;
import asd.project.ast.operation.NotOperation;
import asd.project.compiler.AgentInstructionBaseListener;
import asd.project.compiler.AgentInstructionParser;
import asd.project.compiler.AgentInstructionParser.MoneyContext;
import java.util.Stack;

public class ASTListener extends AgentInstructionBaseListener {

  private final AST ast;

  private final Stack<ASTNode> currentContainer;

  public ASTListener() {
    ast = new AST();
    currentContainer = new Stack<>();
  }

  public AST getAST() {
    return ast;
  }

  @Override
  public void enterFoundBehaviours(AgentInstructionParser.FoundBehavioursContext ctx) {
    ASTNode foundBehaviour = new FoundBehaviours();
    currentContainer.push(foundBehaviour);
  }

  @Override
  public void exitFoundBehaviours(AgentInstructionParser.FoundBehavioursContext ctx) {
    FoundBehaviours foundBehaviours = (FoundBehaviours) currentContainer.pop();
    ast.setRoot(foundBehaviours);
  }

  @Override
  public void enterSentence(AgentInstructionParser.SentenceContext ctx) {
    ASTNode sentence = new Sentence();
    currentContainer.push(sentence);
  }

  @Override
  public void exitSentence(AgentInstructionParser.SentenceContext ctx) {
    Sentence sentence = (Sentence) currentContainer.pop();
    currentContainer.peek().addChild(sentence);
  }

  @Override
  public void enterLogicalExpression(AgentInstructionParser.LogicalExpressionContext ctx) {
    ASTNode expression = new LogicalExpression();
    currentContainer.push(expression);
  }

  @Override
  public void exitLogicalExpression(AgentInstructionParser.LogicalExpressionContext ctx) {
    LogicalExpression expression = (LogicalExpression) currentContainer.pop();
    currentContainer.peek().addChild(expression);
  }

  @Override
  public void enterIfElseExpression(AgentInstructionParser.IfElseExpressionContext ctx) {
    ASTNode expression = new IfElseExpression();
    currentContainer.push(expression);
  }

  @Override
  public void exitIfElseExpression(AgentInstructionParser.IfElseExpressionContext ctx) {
    IfElseExpression expression = (IfElseExpression) currentContainer.pop();
    currentContainer.peek().addChild(expression);
  }

  @Override
  public void enterScalarLiteral(AgentInstructionParser.ScalarLiteralContext ctx) {
    String digits = ctx.getText().replaceAll("[^0-9]", "");

    ScalarLiteral scalar = new ScalarLiteral(digits);
    currentContainer.push(scalar);
  }

  @Override
  public void exitScalarLiteral(AgentInstructionParser.ScalarLiteralContext ctx) {
    ScalarLiteral scalarLiteral = (ScalarLiteral) currentContainer.pop();
    currentContainer.peek().addChild(scalarLiteral);
  }

  @Override
  public void enterHealth(AgentInstructionParser.HealthContext ctx) {
    ASTNode health = new Health();
    currentContainer.push(health);
  }

  @Override
  public void exitHealth(AgentInstructionParser.HealthContext ctx) {
    Health health = (Health) currentContainer.pop();
    currentContainer.peek().addChild(health);
  }

  @Override
  public void enterMoney(MoneyContext ctx) {
    ASTNode money = new Money();
    currentContainer.push(money);
  }

  @Override
  public void exitMoney(MoneyContext ctx) {
    Money money = (Money) currentContainer.pop();
    currentContainer.peek().addChild(money);
  }

  @Override
  public void enterCondition(AgentInstructionParser.ConditionContext ctx) {
    ASTNode condition = new Condition();
    currentContainer.push(condition);
  }

  @Override
  public void exitCondition(AgentInstructionParser.ConditionContext ctx) {
    Condition condition = (Condition) currentContainer.pop();
    currentContainer.peek().addChild(condition);
  }

  @Override
  public void enterNotOperator(AgentInstructionParser.NotOperatorContext ctx) {
    ASTNode notOperation = new NotOperation();
    currentContainer.push(notOperation);
  }

  @Override
  public void exitNotOperator(AgentInstructionParser.NotOperatorContext ctx) {
    NotOperation notOperation = (NotOperation) currentContainer.pop();
    currentContainer.peek().addChild(notOperation);
  }

  @Override
  public void enterLessThan(AgentInstructionParser.LessThanContext ctx) {
    LessThan lessThan = new LessThan();
    currentContainer.push(lessThan);
  }

  @Override
  public void exitLessThan(AgentInstructionParser.LessThanContext ctx) {
    LessThan lessThan = (LessThan) currentContainer.pop();
    currentContainer.peek().addChild(lessThan);
  }

  @Override
  public void enterGreaterThan(AgentInstructionParser.GreaterThanContext ctx) {
    GreaterThan greaterThan = new GreaterThan();
    currentContainer.push(greaterThan);
  }

  @Override
  public void exitGreaterThan(AgentInstructionParser.GreaterThanContext ctx) {
    GreaterThan greaterThan = (GreaterThan) currentContainer.pop();
    currentContainer.peek().addChild(greaterThan);
  }

  @Override
  public void enterAggressive(AgentInstructionParser.AggressiveContext ctx) {
    currentContainer.push(new AggressiveBehaviour());
  }

  @Override
  public void exitAggressive(AgentInstructionParser.AggressiveContext ctx) {
    AggressiveBehaviour foundBehaviour = (AggressiveBehaviour) currentContainer.pop();
    currentContainer.peek().addChild(foundBehaviour);
  }

  @Override
  public void enterDefensive(AgentInstructionParser.DefensiveContext ctx) {
    currentContainer.push(new DefensiveBehaviour());
  }

  @Override
  public void exitDefensive(AgentInstructionParser.DefensiveContext ctx) {
    Behaviour foundBehaviour = (Behaviour) currentContainer.pop();
    currentContainer.peek().addChild(foundBehaviour);
  }

  @Override
  public void enterPassive(AgentInstructionParser.PassiveContext ctx) {
    currentContainer.push(new PassiveBehaviour());
  }

  @Override
  public void exitPassive(AgentInstructionParser.PassiveContext ctx) {
    Behaviour foundBehaviour = (Behaviour) currentContainer.pop();
    currentContainer.peek().addChild(foundBehaviour);
  }
}
