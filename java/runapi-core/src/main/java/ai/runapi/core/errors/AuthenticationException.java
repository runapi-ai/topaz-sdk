package ai.runapi.core.errors;

/** Thrown when API key is missing or invalid. */
public class AuthenticationException extends RunApiException {
  public AuthenticationException(String message) {
    super(message, "authentication", 401, null, null, null);
  }

  public AuthenticationException(
      String message, int statusCode, String requestId, String responseBody, Throwable cause) {
    super(message, "authentication", statusCode, requestId, responseBody, cause);
  }

  public AuthenticationException(
      String message, String code, int statusCode, String requestId, String responseBody, Throwable cause) {
    super(message, code, statusCode, requestId, responseBody, cause);
  }
}
