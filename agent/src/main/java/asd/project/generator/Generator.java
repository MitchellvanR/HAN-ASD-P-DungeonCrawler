package asd.project.generator;

import asd.project.ast.AST;
import asd.project.ast.ASTNode;
import asd.project.ast.Behaviour;
import asd.project.ast.FoundBehaviours;
import asd.project.ast.Sentence;
import asd.project.ast.expression.LogicalExpression;
import asd.project.compiler.Compiler;

public class Generator {

  private final StringBuilder stringBuilder;
  private final Compiler compiler;

  public Generator(Compiler compiler) {
    this.stringBuilder = new StringBuilder();
    this.compiler = compiler;
  }

  public String generate(AST ast) {
    stringBuilder.setLength(0);
    generateFoundBehaviours(ast.getRoot());
    return stringBuilder.toString();
  }

  public void generateFoundBehaviours(FoundBehaviours foundBehaviours) {
    for (ASTNode child : foundBehaviours.getChildren()) {
      if (child instanceof Sentence) {
        generateSentence(child);
      }
    }
  }

  private void generateSentence(ASTNode astNode) {
    for (ASTNode child : astNode.getChildren()) {
      if (child instanceof LogicalExpression) {
        generateBehaviour(child);
      }
    }
  }

  private void generateBehaviour(ASTNode astNode) {
    for (ASTNode child : astNode.getChildren()) {
      if (child instanceof Behaviour behaviour) {
        stringBuilder.append("Behaviour: ")
            .append(child.getNodeLabel());

        compiler.getAgent().setBehaviour(behaviour);
      }
    }
  }

}