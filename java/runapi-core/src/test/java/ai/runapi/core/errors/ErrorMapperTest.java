package ai.runapi.core.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ErrorMapperTest {
  @Test
  void extractsNestedErrorMessage() {
    RunApiException error =
        ErrorMapper.fromResponse(
            400,
            name -> null,
            "{\"error\":{\"message\":\"Bad input\"}}",
            null);

    assertInstanceOf(ValidationException.class, error);
    assertEquals("Bad input", error.getMessage());
    assertEquals("validation", error.getCode());
    assertEquals(400, error.getStatusCode());
  }

  @Test
  void mapsRateLimitAndRetryAfter() {
    RunApiException error =
        ErrorMapper.fromResponse(
            429,
            name -> "retry-after".equalsIgnoreCase(name) ? "2" : null,
            "{\"error\":\"Too many requests\"}",
            null);

    RateLimitException rateLimit = assertInstanceOf(RateLimitException.class, error);
    assertEquals(Duration.ofSeconds(2), rateLimit.getRetryAfter());
  }

  @Test
  void ignoresHtmlBodiesForMessage() {
    RunApiException error =
        ErrorMapper.fromResponse(503, name -> null, "<!doctype html><html>oops</html>", null);

    assertEquals("Service unavailable", error.getMessage());
    assertEquals("service_unavailable", error.getCode());
    assertNull(error.getRequestId());
  }

  @Test
  void usesStatusDefaultForNonJsonNonHtmlBody() {
    // A proxy/CDN plain-text error body must not be surfaced verbatim as the
    // exception message; fall back to the friendly status default. The raw body
    // stays available on getResponseBody() for debugging.
    String rawBody = "upstream connect error or disconnect/reset before headers";
    RunApiException error = ErrorMapper.fromResponse(502, name -> null, rawBody, null);

    assertEquals("Server error", error.getMessage());
    assertEquals(rawBody, error.getResponseBody());
  }
}
