package ai.runapi.core.errors;

/** Thrown when an HTTP request exceeds its configured timeout. */
public class TimeoutException extends RunApiException {
  public TimeoutException(String message, Throwable cause) {
    super(message, "timeout", 0, null, null, cause);
  }
}
