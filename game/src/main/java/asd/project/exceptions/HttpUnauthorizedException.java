package asd.project.exceptions;

public class HttpUnauthorizedException extends Exception {

  public HttpUnauthorizedException(String message) {
    this(message, null);
  }

  public HttpUnauthorizedException(String message, Throwable inner) {
    super(message, inner);
  }
}