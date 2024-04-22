package asd.project.sqlite.exceptions;

public class CouldNotCreatePreparedStatementException extends RuntimeException {

  public CouldNotCreatePreparedStatementException(String errorMessage) {
    super(errorMessage);
  }

}
