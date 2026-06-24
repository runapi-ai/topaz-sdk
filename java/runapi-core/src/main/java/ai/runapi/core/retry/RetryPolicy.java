package ai.runapi.core.retry;

import ai.runapi.core.Constants;
import ai.runapi.core.http.HttpMethod;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/** Retry helpers for transient HTTP failures. */
public final class RetryPolicy {
  private RetryPolicy() {}

  /** Returns whether the method is safe/idempotent for default retry purposes. */
  public static boolean isIdempotent(HttpMethod method) {
    switch (method) {
      case GET:
      case HEAD:
      case PUT:
      case DELETE:
      case OPTIONS:
        return true;
      default:
        return false;
    }
  }

  /** Returns whether the HTTP status should be retried when the method is retryable. */
  public static boolean isRetryableStatus(int statusCode) {
    return statusCode == 429 || statusCode >= 500;
  }

  /** Computes exponential backoff plus 0-50% jitter. */
  public static Duration delay(int attempt, Duration baseDelay, Duration maxDelay) {
    if (attempt < 0) {
      throw new IllegalArgumentException("attempt must be >= 0");
    }
    long baseMillis = baseDelay.toMillis();
    long maxMillis = maxDelay.toMillis();
    long exponential = baseMillis;
    for (int i = 0; i < attempt; i++) {
      exponential = Math.min(maxMillis, exponential * 2L);
    }
    long capped = Math.min(exponential, maxMillis);
    long jitter = capped == 0 ? 0 : ThreadLocalRandom.current().nextLong(capped / 2L + 1L);
    return Duration.ofMillis(Math.min(maxMillis, capped + jitter));
  }

  /** Computes default exponential backoff plus jitter. */
  public static Duration delay(int attempt) {
    return delay(attempt, Constants.DEFAULT_RETRY_BASE_DELAY, Constants.DEFAULT_RETRY_MAX_DELAY);
  }
}
