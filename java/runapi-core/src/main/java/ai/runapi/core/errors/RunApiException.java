package ai.runapi.core.errors;

import org.jspecify.annotations.Nullable;

/** Base exception for all RunAPI Java SDK errors. */
public class RunApiException extends RuntimeException {
  private final @Nullable String code;
  private final int statusCode;
  private final @Nullable String requestId;
  private final @Nullable String responseBody;

  public RunApiException(
      String message,
      @Nullable String code,
      int statusCode,
      @Nullable String requestId,
      @Nullable String responseBody,
      @Nullable Throwable cause) {
    super(message, cause);
    this.code = code;
    this.statusCode = statusCode;
    this.requestId = requestId;
    this.responseBody = responseBody;
  }

  /** Explicit machine-readable reason, if one was provided. */
  public @Nullable String getCode() {
    return code;
  }

  /** HTTP status code, or {@code 0} when unavailable. */
  public int getStatusCode() {
    return statusCode;
  }

  /** Request ID from response headers, if present. */
  public @Nullable String getRequestId() {
    return requestId;
  }

  /** Raw HTTP response body, if available. */
  public @Nullable String getResponseBody() {
    return responseBody;
  }
}
