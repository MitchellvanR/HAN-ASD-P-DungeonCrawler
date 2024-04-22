package asd.project.exceptions;

public class HttpRequestException extends Exception {

  public HttpRequestException(String message) {
    this(message, null);
  }

  public HttpRequestException(String message, Throwable inner) {
    super(message, inner);
  }
}