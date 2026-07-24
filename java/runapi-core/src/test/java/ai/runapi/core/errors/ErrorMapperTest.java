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
    assertNull(error.getCode());
    assertEquals(400, error.getStatusCode());
  }

  @Test
  void preservesExplicitHttpCodeAndLeavesMissingCodeNull() {
    RunApiException explicit =
        ErrorMapper.fromResponse(
            409,
            name -> null,
            "{\"error\":{\"code\":\"source_task_not_ready\",\"message\":\"wait\"}}",
            null);
    RunApiException missing =
        ErrorMapper.fromResponse(409, name -> null, "{\"error\":{\"message\":\"wait\"}}", null);

    assertEquals("source_task_not_ready", explicit.getCode());
    assertNull(missing.getCode());
  }

  @Test
  void preservesContinuationCodesAndClassifiesByStatus() {
    Object[][] cases = {
      {400, "invalid_resource_id", ValidationException.class},
      {409, "request_conflict", RunApiException.class},
      {409, "source_task_not_ready", RunApiException.class},
      {422, "source_task_unusable", ValidationException.class},
      {422, "continuation_not_supported", ValidationException.class},
      {429, "rate_limited", RateLimitException.class},
      {503, "continuation_unavailable", RunApiException.class}
    };

    for (Object[] testCase : cases) {
      int status = (int) testCase[0];
      String code = (String) testCase[1];
      Class<?> errorClass = (Class<?>) testCase[2];
      RunApiException error =
          ErrorMapper.fromResponse(
              status,
              name -> null,
              "{\"error\":{\"code\":\"" + code + "\",\"message\":\"failed\"}}",
              null);

      assertInstanceOf(errorClass, error);
      assertEquals(status, error.getStatusCode());
      assertEquals(code, error.getCode());
    }
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
    assertNull(error.getCode());
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
