package asd.project.ast;

import asd.project.checker.SemanticError;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AST {

  private FoundBehaviours root;

  public AST() {
    root = new FoundBehaviours();
  }

  public AST(FoundBehaviours behaviour) {
    root = behaviour;
  }

  public FoundBehaviours getRoot() {
    return root;
  }

  public void setRoot(FoundBehaviours behaviour) {
    root = behaviour;
  }

  public List<SemanticError> getErrors() {
    ArrayList<SemanticError> errors = new ArrayList<>();
    collectErrors(errors, root);
    return errors;
  }

  private void collectErrors(ArrayList<SemanticError> errors, ASTNode node) {
    if (node == null) {
      root.setError("One of nodes of the AST is null");
      return;
    }

    if (node.hasError()) {
      errors.add(node.getError());
    }
    for (ASTNode child : node.getChildren()) {
      collectErrors(errors, child);
    }
  }

  @Override
  public String toString() {
    return root.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AST ast = (AST) o;
    return Objects.equals(root, ast.root);
  }

  @Override
  public int hashCode() {
    return Objects.hash(root);
  }
}
