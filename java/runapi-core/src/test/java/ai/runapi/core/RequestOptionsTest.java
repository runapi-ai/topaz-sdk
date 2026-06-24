package ai.runapi.core;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class RequestOptionsTest {
  @Test
  void noneReturnsSingleton() {
    assertSame(RequestOptions.none(), RequestOptions.none());
  }

  @Test
  void storesHeadersAndOverrides() {
    RequestOptions options =
        RequestOptions.builder()
            .header("X-Test", "one")
            .header("x-test", "two")
            .maxRetries(0)
            .timeout(Duration.ofSeconds(3))
            .build();

    assertSame(Integer.valueOf(0), options.getMaxRetries());
    org.junit.jupiter.api.Assertions.assertEquals("two", options.getHeaders().get("x-test"));
    org.junit.jupiter.api.Assertions.assertEquals(Duration.ofSeconds(3), options.getTimeout());
  }

  @Test
  void rejectsInvalidValues() {
    assertThrows(IllegalArgumentException.class, () -> RequestOptions.builder().maxRetries(-1));
    assertThrows(
        IllegalArgumentException.class,
        () -> RequestOptions.builder().timeout(Duration.ZERO));
  }
}
