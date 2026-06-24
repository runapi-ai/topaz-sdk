package ai.runapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.errors.AuthenticationException;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class ClientOptionsTest {
  @Test
  void resolvesExplicitApiKeyAndDefaults() {
    ClientOptions options = ClientOptions.builder().apiKey("  sk-test  ").build();

    assertEquals("sk-test", options.getApiKey());
    assertEquals(URI.create("https://runapi.ai"), options.getBaseUrl());
    assertEquals(Duration.ofMinutes(15), options.getTimeout());
    assertEquals(2, options.getMaxRetries());
  }

  @Test
  void rejectsMissingApiKeyAtBuildTime() {
    assertThrows(AuthenticationException.class, () -> ClientOptions.builder().build());
  }

  @Test
  void validatesBaseUrlSchemeAndHost() {
    ClientOptions.Builder builder = ClientOptions.builder().apiKey("sk-test");

    assertThrows(IllegalArgumentException.class, () -> builder.baseUrl("ftp://runapi.ai"));
    assertThrows(IllegalArgumentException.class, () -> builder.baseUrl("https:///missing-host"));
  }

  @Test
  void baseClientBuilderExposesClientLevelOptions() {
    ClientOptions options =
        BaseClient.builder()
            .apiKey("sk-test")
            .baseUrl(URI.create("https://api.runapi.ai"))
            .timeout(Duration.ofSeconds(10))
            .maxRetries(4)
            .retryBaseDelay(Duration.ofMillis(100))
            .retryMaxDelay(Duration.ofSeconds(2))
            .pollingInterval(Duration.ofSeconds(3))
            .pollingMaxWait(Duration.ofMinutes(7))
            .header("X-Test", "yes")
            .transport(new NoopTransport())
            .build()
            .options();

    assertEquals(URI.create("https://api.runapi.ai"), options.getBaseUrl());
    assertEquals(Duration.ofSeconds(10), options.getTimeout());
    assertEquals(4, options.getMaxRetries());
    assertEquals(Duration.ofMillis(100), options.getRetryBaseDelay());
    assertEquals(Duration.ofSeconds(2), options.getRetryMaxDelay());
    assertEquals(Duration.ofSeconds(3), options.getPollingInterval());
    assertEquals(Duration.ofMinutes(7), options.getPollingMaxWait());
    assertEquals("yes", options.getHeaders().get("X-Test"));
  }

  private static final class NoopTransport implements HttpTransport {
    @Override
    public HttpResponse send(ai.runapi.core.http.HttpRequest request) {
      return new HttpResponse(200, "{}", Collections.<String, java.util.List<String>>emptyMap());
    }

    @Override
    public void close() {}
  }
}
