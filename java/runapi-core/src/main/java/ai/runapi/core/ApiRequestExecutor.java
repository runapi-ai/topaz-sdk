package ai.runapi.core;

import ai.runapi.core.errors.NetworkException;
import ai.runapi.core.errors.RateLimitException;
import ai.runapi.core.errors.RunApiException;
import ai.runapi.core.errors.TimeoutException;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.json.Json;
import ai.runapi.core.retry.RetryPolicy;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Executes SDK HTTP requests and decodes JSON responses. */
public final class ApiRequestExecutor {
  private final HttpTransport transport;
  private final ClientOptions options;

  public ApiRequestExecutor(HttpTransport transport, ClientOptions options) {
    this.transport = Objects.requireNonNull(transport, "transport");
    this.options = Objects.requireNonNull(options, "options");
  }

  /** Sends a request and decodes its JSON body as {@code responseType}. */
  public <T> T send(HttpRequest request, Class<T> responseType) {
    Objects.requireNonNull(responseType, "responseType");
    HttpResponse response = send(request);
    try {
      return Json.mapper().readValue(response.getBody(), responseType);
    } catch (IOException e) {
      throw new RunApiException("Failed to decode response", "decode_error", response.getStatusCode(), null, response.getBody(), e);
    }
  }

  /** Sends a request and returns the raw HTTP response. */
  public HttpResponse send(HttpRequest request) {
    Objects.requireNonNull(request, "request");
    int maxRetries = maxRetriesFor(request);
    int attempt = 0;
    while (true) {
      try {
        return transport.send(request);
      } catch (RateLimitException e) {
        if (!shouldRetry(request, e, attempt, maxRetries)) {
          throw e;
        }
        Duration delay = retryDelay(e, attempt);
        if (delay == null) {
          // Server asked to wait longer than the configured max backoff; retrying
          // sooner would just re-hit the rate limit, so surface the error instead.
          throw e;
        }
        sleep(delay);
      } catch (TimeoutException e) {
        if (!shouldRetry(request, e, attempt, maxRetries)) {
          throw e;
        }
        sleep(RetryPolicy.delay(attempt, options.getRetryBaseDelay(), options.getRetryMaxDelay()));
      } catch (NetworkException e) {
        if (!shouldRetry(request, e, attempt, maxRetries)) {
          throw e;
        }
        sleep(RetryPolicy.delay(attempt, options.getRetryBaseDelay(), options.getRetryMaxDelay()));
      } catch (RunApiException e) {
        if (!shouldRetry(request, e, attempt, maxRetries)) {
          throw e;
        }
        sleep(RetryPolicy.delay(attempt, options.getRetryBaseDelay(), options.getRetryMaxDelay()));
      }
      attempt++;
    }
  }

  private int maxRetriesFor(HttpRequest request) {
    Integer override = request.getOptions().getMaxRetries();
    return override == null ? options.getMaxRetries() : override;
  }

  private boolean shouldRetry(HttpRequest request, RunApiException error, int attempt, int maxRetries) {
    if (attempt >= maxRetries) {
      return false;
    }
    if (!RetryPolicy.isIdempotent(request.getMethod())) {
      return false;
    }
    if (error instanceof NetworkException || error instanceof TimeoutException) {
      return true;
    }
    return RetryPolicy.isRetryableStatus(error.getStatusCode());
  }

  /**
   * Returns how long to wait before retrying a rate-limited request, or {@code null}
   * to signal the caller should stop retrying.
   *
   * <p>A server {@code Retry-After} is honored up to the configured {@code retryMaxDelay}.
   * If the server asks to wait longer than {@code retryMaxDelay}, retrying sooner would
   * just re-hit the rate limit (and amplify load), and waiting the full duration could
   * pin the thread for a hostile/far-future value — so we give up and let the
   * {@link RateLimitException} surface instead.
   */
  @Nullable
  Duration retryDelay(RateLimitException error, int attempt) {
    Duration retryAfter = error.getRetryAfter();
    if (retryAfter != null) {
      if (retryAfter.isNegative()) {
        return Duration.ZERO;
      }
      return retryAfter.compareTo(options.getRetryMaxDelay()) > 0 ? null : retryAfter;
    }
    return RetryPolicy.delay(attempt, options.getRetryBaseDelay(), options.getRetryMaxDelay());
  }

  private static void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
