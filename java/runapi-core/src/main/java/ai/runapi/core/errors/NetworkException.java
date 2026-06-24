package ai.runapi.core.errors;

/** Thrown when an HTTP request cannot be sent or read. */
public class NetworkException extends RunApiException {
  public NetworkException(String message, Throwable cause) {
    super(message, "network", 0, null, null, cause);
  }
}
