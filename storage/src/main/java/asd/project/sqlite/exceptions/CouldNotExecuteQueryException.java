package asd.project.sqlite.exceptions;

public class CouldNotExecuteQueryException extends RuntimeException {

  public CouldNotExecuteQueryException(String errorMessage) {
    super(errorMessage);
  }

}
