package ai.runapi.core.retry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.runapi.core.http.HttpMethod;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class RetryPolicyTest {
  @Test
  void retriesOnlyIdempotentMethodsByDefault() {
    assertTrue(RetryPolicy.isIdempotent(HttpMethod.GET));
    assertFalse(RetryPolicy.isIdempotent(HttpMethod.POST));
  }

  @Test
  void detectsRetryableStatusCodes() {
    assertTrue(RetryPolicy.isRetryableStatus(429));
    assertTrue(RetryPolicy.isRetryableStatus(503));
    assertFalse(RetryPolicy.isRetryableStatus(422));
  }

  @Test
  void capsDelay() {
    Duration delay = RetryPolicy.delay(10, Duration.ofMillis(500), Duration.ofSeconds(5));
    assertTrue(delay.compareTo(Duration.ofSeconds(5)) <= 0);
  }
}
