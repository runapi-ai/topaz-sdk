package ai.runapi.core.errors;

/** Thrown when request validation fails. */
public class ValidationException extends RunApiException {
  public ValidationException(String message) {
    super(message, "validation", 422, null, null, null);
  }

  public ValidationException(
      String message, int statusCode, String requestId, String responseBody, Throwable cause) {
    super(message, "validation", statusCode, requestId, responseBody, cause);
  }
}
