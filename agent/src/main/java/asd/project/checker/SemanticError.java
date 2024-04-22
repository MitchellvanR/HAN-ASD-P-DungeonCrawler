package asd.project.checker;

public class SemanticError {

  private final String description;

  public SemanticError(String description) {
    this.description = description;
  }

  public String toString() {
    return "ERROR: " + description;
  }
}