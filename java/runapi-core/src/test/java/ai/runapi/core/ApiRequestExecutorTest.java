package ai.runapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.errors.NetworkException;
import ai.runapi.core.errors.RateLimitException;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ApiRequestExecutorTest {
  @Test
  void retriesIdempotentRequests() {
    AtomicInteger calls = new AtomicInteger();
    HttpTransport transport =
        new StubTransport() {
          @Override
          public HttpResponse send(HttpRequest request) {
            if (calls.incrementAndGet() == 1) {
              throw new NetworkException("temporary", null);
            }
            return json("{\"ok\":true}");
          }
        };
    ClientOptions options =
        ClientOptions.builder()
            .apiKey("sk-test")
            .retryBaseDelay(Duration.ofMillis(1))
            .retryMaxDelay(Duration.ofMillis(1))
            .build();

    new ApiRequestExecutor(transport, options).send(HttpRequest.builder(HttpMethod.GET, "/ok").build());

    assertEquals(2, calls.get());
  }

  @Test
  void doesNotRetryPostByDefault() {
    AtomicInteger calls = new AtomicInteger();
    HttpTransport transport =
        new StubTransport() {
          @Override
          public HttpResponse send(HttpRequest request) {
            calls.incrementAndGet();
            throw new NetworkException("temporary", null);
          }
        };
    ClientOptions options =
        ClientOptions.builder()
            .apiKey("sk-test")
            .retryBaseDelay(Duration.ofMillis(1))
            .retryMaxDelay(Duration.ofMillis(1))
            .build();

    assertThrows(
        NetworkException.class,
        () -> new ApiRequestExecutor(transport, options).send(HttpRequest.builder(HttpMethod.POST, "/no").build()));
    assertEquals(1, calls.get());
  }

  @Test
  void retryAfterHonoredUpToRetryMaxDelay() {
    ApiRequestExecutor executor =
        new ApiRequestExecutor(
            new StubTransport() {
              @Override
              public HttpResponse send(HttpRequest request) {
                return json("{\"ok\":true}");
              }
            },
            ClientOptions.builder().apiKey("sk-test").retryMaxDelay(Duration.ofSeconds(5)).build());
    RateLimitException error = new RateLimitException("slow down", 429, null, null, Duration.ofSeconds(2), null);

    // Retry-After within the max-backoff ceiling is honored verbatim.
    assertEquals(Duration.ofSeconds(2), executor.retryDelay(error, 0));
  }

  @Test
  void retryAfterBeyondRetryMaxDelayStopsRetrying() {
    AtomicInteger calls = new AtomicInteger();
    HttpTransport transport =
        new StubTransport() {
          @Override
          public HttpResponse send(HttpRequest request) {
            calls.incrementAndGet();
            throw new RateLimitException("slow down", 429, null, null, Duration.ofSeconds(120), null);
          }
        };
    ClientOptions options =
        ClientOptions.builder().apiKey("sk-test").retryMaxDelay(Duration.ofSeconds(5)).build();
    ApiRequestExecutor executor = new ApiRequestExecutor(transport, options);

    // Retry-After (120s) exceeds retryMaxDelay (5s): give up immediately instead of
    // shortening the wait and re-hitting the rate limit on every retry.
    assertThrows(
        RateLimitException.class,
        () -> executor.send(HttpRequest.builder(HttpMethod.GET, "/rate-limited").build()));
    assertEquals(1, calls.get());
  }

  private static HttpResponse json(String body) {
    return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
  }

  private abstract static class StubTransport implements HttpTransport {
    @Override
    public void close() {}
  }
}
