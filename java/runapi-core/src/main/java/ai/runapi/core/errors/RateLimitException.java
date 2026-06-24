package ai.runapi.core.errors;

import java.time.Duration;
import org.jspecify.annotations.Nullable;

/** Thrown when rate limit is exceeded. */
public class RateLimitException extends RunApiException {
  private final @Nullable Duration retryAfter;

  public RateLimitException(
      String message,
      int statusCode,
      String requestId,
      String responseBody,
      @Nullable Duration retryAfter,
      Throwable cause) {
    super(message, "rate_limit", statusCode, requestId, responseBody, cause);
    this.retryAfter = retryAfter;
  }

  /** Server-suggested retry delay, if provided. */
  public @Nullable Duration getRetryAfter() {
    return retryAfter;
  }
}
