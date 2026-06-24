package ai.runapi.core.errors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** Maps HTTP error responses to SDK exception subclasses. */
public final class ErrorMapper {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private ErrorMapper() {}

  /** Builds a RunAPI exception from an HTTP response. */
  public static RunApiException fromResponse(
      int statusCode, Headers headers, String responseBody, Throwable cause) {
    String requestId = headers.firstValue("x-request-id");
    String message = extractMessage(responseBody);
    if (message == null || message.trim().isEmpty()) {
      message = defaultMessageForStatus(statusCode);
    }

    if (statusCode == 401) {
      return new AuthenticationException(message, statusCode, requestId, responseBody, cause);
    }
    if (statusCode == 400 || statusCode == 422) {
      return new ValidationException(message, statusCode, requestId, responseBody, cause);
    }
    if (statusCode == 429) {
      return new RateLimitException(
          message, statusCode, requestId, responseBody, parseRetryAfter(headers.firstValue("retry-after")), cause);
    }
    return new RunApiException(
        message, codeForStatus(statusCode), statusCode, requestId, responseBody, cause);
  }

  private static String codeForStatus(int statusCode) {
    if (statusCode == 402) {
      return "insufficient_credits";
    }
    if (statusCode == 404) {
      return "not_found";
    }
    if (statusCode == 409) {
      return "conflict";
    }
    if (statusCode == 503) {
      return "service_unavailable";
    }
    if (statusCode >= 500) {
      return "server";
    }
    return "runapi_error";
  }

  private static String defaultMessageForStatus(int statusCode) {
    switch (statusCode) {
      case 400:
        return "Bad request";
      case 401:
        return "Unauthorized";
      case 402:
        return "Insufficient credits";
      case 404:
        return "Not found";
      case 408:
        return "Request timeout";
      case 409:
        return "Conflict";
      case 413:
        return "Payload too large";
      case 415:
        return "Unsupported media type";
      case 422:
        return "Validation failed";
      case 429:
        return "Too many requests";
      case 503:
        return "Service unavailable";
      default:
        return statusCode >= 500 ? "Server error" : "Request failed";
    }
  }

  private static String extractMessage(String responseBody) {
    if (responseBody == null || responseBody.trim().isEmpty() || looksLikeHtml(responseBody)) {
      return null;
    }
    try {
      JsonNode root = MAPPER.readTree(responseBody);
      String fromError = extract(root.get("error"));
      if (fromError != null) {
        return fromError;
      }
      JsonNode errors = root.get("errors");
      if (errors != null && errors.isArray() && errors.size() > 0) {
        String fromErrors = extract(errors.get(0));
        if (fromErrors != null) {
          return fromErrors;
        }
      }
      for (String key : new String[] {"message", "detail", "errorMessage", "msg"}) {
        String value = extract(root.get(key));
        if (value != null) {
          return value;
        }
      }
      return null;
    } catch (Exception ignored) {
      // Non-JSON, non-HTML body (e.g. a proxy/CDN plain-text or XML error page).
      // Do not surface the raw body as the exception message: it can be large and
      // leak infrastructure details. Fall back to the friendly status default; the
      // raw body is still retained on the exception's responseBody field.
      return null;
    }
  }

  private static String extract(JsonNode node) {
    if (node == null || node.isNull()) {
      return null;
    }
    if (node.isTextual()) {
      String text = node.asText().trim();
      return text.isEmpty() ? null : text;
    }
    if (node.isObject()) {
      for (String key : new String[] {"message", "detail"}) {
        String value = extract(node.get(key));
        if (value != null) {
          return value;
        }
      }
    }
    return null;
  }

  private static boolean looksLikeHtml(String body) {
    String lower = body.toLowerCase();
    return lower.contains("<!doctype") || lower.contains("<html");
  }

  private static Duration parseRetryAfter(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    String trimmed = value.trim();
    try {
      return Duration.ofSeconds(Long.parseLong(trimmed));
    } catch (NumberFormatException ignored) {
      try {
        ZonedDateTime date = ZonedDateTime.parse(trimmed, DateTimeFormatter.RFC_1123_DATE_TIME);
        Duration duration = Duration.between(ZonedDateTime.now(date.getZone()), date);
        return duration.isNegative() ? Duration.ZERO : duration;
      } catch (Exception ignoredAgain) {
        return null;
      }
    }
  }

  /** Header lookup abstraction for transports. */
  public interface Headers {
    String firstValue(String name);
  }
}
