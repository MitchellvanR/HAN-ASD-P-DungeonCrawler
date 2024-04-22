package asd.project.exceptions;

public class UnknownEventException extends RuntimeException {

  public UnknownEventException() {
    super("The given event is not found");
  }
}
