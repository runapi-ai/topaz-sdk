package ai.runapi.core.polling;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.RunApiException;
import ai.runapi.core.errors.TaskFailedException;
import ai.runapi.core.errors.TaskTimeoutException;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/** A terminal result or accepted Task that can be observed through {@link #subscribe()}. */
public final class Task<T> {
  private final ApiRequestExecutor executor;
  private final RequestOptions options;
  private final Class<T> responseType;
  private final Duration pollingInterval;
  private final Duration pollingMaxWait;
  private final @Nullable String id;
  private final @Nullable String location;
  private final @Nullable T terminalResponse;
  private final @Nullable Duration initialDelay;

  private Task(
      ApiRequestExecutor executor,
      RequestOptions options,
      Class<T> responseType,
      Duration pollingInterval,
      Duration pollingMaxWait,
      @Nullable String id,
      @Nullable String location,
      @Nullable T terminalResponse,
      @Nullable Duration initialDelay) {
    this.executor = executor;
    this.options = options;
    this.responseType = responseType;
    this.pollingInterval = pollingInterval;
    this.pollingMaxWait = pollingMaxWait;
    this.id = id;
    this.location = location;
    this.terminalResponse = terminalResponse;
    this.initialDelay = initialDelay;
  }

  /** Starts a hybrid operation, returning a terminal result or an accepted Task. */
  public static <T> Task<T> start(
      ApiRequestExecutor executor,
      HttpRequest request,
      Class<T> responseType,
      Duration pollingInterval,
      Duration pollingMaxWait) {
    ApiRequestExecutor checkedExecutor = Objects.requireNonNull(executor, "executor");
    HttpRequest checkedRequest = withIdempotencyKey(Objects.requireNonNull(request, "request"));
    Class<T> checkedResponseType = Objects.requireNonNull(responseType, "responseType");
    Duration checkedInterval = Objects.requireNonNull(pollingInterval, "pollingInterval");
    Duration checkedMaxWait = Objects.requireNonNull(pollingMaxWait, "pollingMaxWait");
    HttpResponse response = checkedExecutor.send(checkedRequest);

    if (response.getStatusCode() != 202) {
      return new Task<T>(
          checkedExecutor,
          checkedRequest.getOptions(),
          checkedResponseType,
          checkedInterval,
          checkedMaxWait,
          null,
          null,
          decodeResponse(response, checkedResponseType),
          null);
    }

    JsonNode acceptance = json(response);
    String id = requiredText(acceptance, "id", "Task acceptance is missing id");
    String location = response.firstHeader("Location");
    if (location == null || location.trim().isEmpty()) {
      throw new RunApiException("Task acceptance is missing Location", "task_location_missing", 202, null, response.getBody(), null);
    }
    return new Task<T>(
        checkedExecutor,
        checkedRequest.getOptions(),
        checkedResponseType,
        checkedInterval,
        checkedMaxWait,
        id,
        location.trim(),
        null,
        retryAfter(response));
  }

  /** Returns the accepted Task ID, or {@code null} when the initial response was terminal. */
  public @Nullable String getId() {
    return id;
  }

  /** Returns the opaque Task Result URL, or {@code null} when the initial response was terminal. */
  public @Nullable String getLocation() {
    return location;
  }

  /** Returns whether the initial request completed without Task polling. */
  public boolean isTerminal() {
    return terminalResponse != null;
  }

  /** Observes this Task until it produces its terminal endpoint response. */
  public T subscribe() {
    if (terminalResponse != null) {
      return terminalResponse;
    }

    Instant deadline = Instant.now().plus(pollingMaxWait);
    Duration delay = initialDelay == null ? pollingInterval : initialDelay;
    while (true) {
      waitBeforePoll(delay, deadline);
      HttpResponse response = executor.send(
          HttpRequest.builder(HttpMethod.GET, Objects.requireNonNull(location, "location"))
              .options(pollingOptions(deadline))
              .build());
      JsonNode taskResult = json(response);
      String status = requiredText(taskResult, "status", "Task Result is missing status");
      String normalized = Poller.normalize(new TaskStatus(status));
      if ("completed".equals(normalized)) {
        return decodeEnvelope(taskResult, responseType);
      }
      if ("failed".equals(normalized)) {
        throw new TaskFailedException(failureMessage(taskResult), taskResult);
      }
      delay = retryAfter(response);
      if (delay == null) {
        delay = pollingInterval;
      }
    }
  }

  private static HttpRequest withIdempotencyKey(HttpRequest request) {
    if (request.getMethod() != HttpMethod.POST || hasHeader(request.getHeaders(), "Idempotency-Key")
        || hasHeader(request.getOptions().getHeaders(), "Idempotency-Key")) {
      return request;
    }

    HttpRequest.Builder builder = HttpRequest.builder(request.getMethod(), request.getPath()).options(request.getOptions());
    for (Map.Entry<String, String> query : request.getQuery().entrySet()) {
      builder.query(query.getKey(), query.getValue());
    }
    for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
      builder.header(header.getKey(), header.getValue());
    }
    if (request.getBody() != null) {
      builder.body(request.getBody());
    }
    if (request.allowsNotModified()) {
      builder.allowNotModified();
    }
    return builder.header("Idempotency-Key", UUID.randomUUID().toString()).build();
  }

  private static boolean hasHeader(Map<String, String> headers, String name) {
    for (String candidate : headers.keySet()) {
      if (candidate.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  private static <T> T decodeResponse(HttpResponse response, Class<T> responseType) {
    String contentType = response.firstHeader("Content-Type");
    if (isJson(contentType)) {
      try {
        return Json.mapper().readValue(response.getBodyBytes(), responseType);
      } catch (IOException e) {
        throw new RunApiException("Failed to decode response", "decode_error", response.getStatusCode(), null, response.getBody(), e);
      }
    }
    if (responseType == String.class) {
      return responseType.cast(response.getBody());
    }
    if (responseType == byte[].class) {
      return responseType.cast(response.getBodyBytes());
    }
    throw new RunApiException("Response content type requires String or byte[] result type", "decode_error", response.getStatusCode(), null, response.getBody(), null);
  }

  private static <T> T decodeEnvelope(JsonNode taskResult, Class<T> responseType) {
    JsonNode envelope = taskResult.get("response");
    if (envelope == null || !envelope.isObject()) {
      throw new RunApiException("Task Result is missing response", "task_response_missing", 200, null, taskResult.toString(), null);
    }
    JsonNode contentType = envelope.get("content_type");
    JsonNode body = envelope.get("body");
    if (contentType == null || !contentType.isTextual() || body == null) {
      throw new RunApiException("Task Result response is invalid", "task_response_invalid", 200, null, envelope.toString(), null);
    }
    if (isJson(contentType.textValue())) {
      try {
        return Json.mapper().treeToValue(body, responseType);
      } catch (IOException e) {
        throw new RunApiException("Failed to decode Task Result", "decode_error", 200, null, body.toString(), e);
      }
    }
    if (responseType == String.class) {
      return responseType.cast(body.isTextual() ? body.textValue() : body.toString());
    }
    if (responseType == byte[].class) {
      String text = body.isTextual() ? body.textValue() : body.toString();
      return responseType.cast(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
    throw new RunApiException("Task Result content type requires String or byte[] result type", "decode_error", 200, null, body.toString(), null);
  }

  private static JsonNode json(HttpResponse response) {
    try {
      return Json.mapper().readTree(response.getBodyBytes());
    } catch (IOException e) {
      throw new RunApiException("Failed to decode Task response", "decode_error", response.getStatusCode(), null, response.getBody(), e);
    }
  }

  private static String requiredText(JsonNode node, String field, String message) {
    JsonNode value = node.get(field);
    if (value == null || !value.isTextual() || value.textValue().trim().isEmpty()) {
      throw new RunApiException(message, "task_response_invalid", 200, null, node.toString(), null);
    }
    return value.textValue();
  }

  private static String failureMessage(JsonNode taskResult) {
    JsonNode body = taskResult.path("response").path("body");
    JsonNode message = body.path("error").path("message");
    if (!message.isTextual()) {
      message = body.path("message");
    }
    return message.isTextual() && !message.textValue().trim().isEmpty() ? message.textValue() : "Task failed";
  }

  private static boolean isJson(@Nullable String contentType) {
    if (contentType == null || contentType.trim().isEmpty()) {
      return true;
    }
    String mediaType = contentType.split(";", 2)[0].trim().toLowerCase(Locale.ROOT);
    return "application/json".equals(mediaType) || mediaType.endsWith("+json");
  }

  private static @Nullable Duration retryAfter(HttpResponse response) {
    String value = response.firstHeader("Retry-After");
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    try {
      return Duration.ofSeconds(Math.max(0L, Long.parseLong(value.trim())));
    } catch (NumberFormatException ignored) {
      try {
        Duration delay = Duration.between(Instant.now(), ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant());
        return delay.isNegative() ? Duration.ZERO : delay;
      } catch (DateTimeParseException ignoredAgain) {
        return null;
      }
    }
  }

  private RequestOptions pollingOptions(Instant deadline) {
    Duration remaining = remaining(deadline);
    RequestOptions.Builder builder = RequestOptions.builder().headers(options.getHeaders()).maxRetries(0);
    Duration configuredTimeout = options.getTimeout();
    builder.timeout(configuredTimeout == null || configuredTimeout.compareTo(remaining) > 0 ? remaining : configuredTimeout);
    if (options.getPollingInterval() != null) {
      builder.pollingInterval(options.getPollingInterval());
    }
    if (options.getPollingMaxWait() != null) {
      builder.pollingMaxWait(options.getPollingMaxWait());
    }
    return builder.build();
  }

  private static void waitBeforePoll(Duration requested, Instant deadline) {
    Duration remaining = remaining(deadline);
    if (requested.compareTo(remaining) >= 0) {
      sleep(remaining);
      throw new TaskTimeoutException("Task polling timed out", null);
    }
    sleep(requested);
    ensureBeforeDeadline(deadline);
  }

  private static Duration remaining(Instant deadline) {
    Duration remaining = Duration.between(Instant.now(), deadline);
    if (remaining.isZero() || remaining.isNegative()) {
      throw new TaskTimeoutException("Task polling timed out", null);
    }
    return remaining;
  }

  private static void ensureBeforeDeadline(Instant deadline) {
    if (!Instant.now().isBefore(deadline)) {
      throw new TaskTimeoutException("Task polling timed out", null);
    }
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
