package asd.project.parser;

import asd.project.ast.AST;
import asd.project.ast.AgentFeature;
import asd.project.ast.Behaviour;
import asd.project.ast.Condition;
import asd.project.ast.Expression;
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

public class CompilerFixtures {
  public static AST uncheckedLevel0() {
    FoundBehaviours foundBehaviours = new FoundBehaviours();
    foundBehaviours.addChild((new Sentence())
        .addChild(new IfElseExpression()
            .addChild(new Condition()
                .addChild(new Health())
                .addChild(new LessThan())
                .addChild(new ScalarLiteral(5)))
            .addChild(new Sentence()
                .addChild(new LogicalExpression()
                    .addChild(new AggressiveBehaviour())))));


    return new AST(foundBehaviours);
  }

  public static AST uncheckedLevel1() {
    FoundBehaviours foundBehaviours = new FoundBehaviours();
    foundBehaviours.addChild((new Sentence()
        .addChild(new LogicalExpression()
            .addChild(new AggressiveBehaviour()))
        .addChild(new LogicalExpression()
            .addChild(new PassiveBehaviour()))
        .addChild(new LogicalExpression()
            .addChild(new DefensiveBehaviour()))));
    return new AST(foundBehaviours);
  }

  public static AST uncheckedLevel2() {
    FoundBehaviours foundBehaviours = new FoundBehaviours();
    foundBehaviours.addChild((new Sentence()
        .addChild(new LogicalExpression()
            .addChild(new NotOperation())
            .addChild(new AggressiveBehaviour())))
        .addChild(new LogicalExpression()
            .addChild(new DefensiveBehaviour())));
    return new AST(foundBehaviours);
  }

  public static AST uncheckedLevel3() {

    FoundBehaviours foundBehaviours = new FoundBehaviours();
    foundBehaviours.addChild((new Sentence())
        .addChild(new IfElseExpression()
            .addChild(new Condition()
                .addChild(new Money())
                .addChild(new GreaterThan())
                .addChild(new ScalarLiteral(100)))
            .addChild(new Sentence()
                .addChild(new LogicalExpression()
                    .addChild(new AggressiveBehaviour())))
            .addChild(new Sentence()
                .addChild(new LogicalExpression()
                    .addChild(new PassiveBehaviour())))));


    return new AST(foundBehaviours);

  }
}
