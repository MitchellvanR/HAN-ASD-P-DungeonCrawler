package asd.project.ast;

import java.util.ArrayList;

public class Sentence extends ASTNode {

  private ArrayList<ASTNode> children;

  public Sentence() {
    super();
    this.children = new ArrayList<>();
  }

  @Override
  public String getNodeLabel() {
    return "Sentence";
  }

  @Override
  public ASTNode addChild(ASTNode child) {
    children.add(child);

    return this;
  }

  @Override
  public ASTNode removeChild(ASTNode child) {
    children.remove(child);

    return this;
  }

  @Override
  public ArrayList<ASTNode> getChildren() {
    return this.children;
  }

}
