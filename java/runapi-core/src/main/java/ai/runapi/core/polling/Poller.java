package ai.runapi.core.polling;

import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.errors.TaskFailedException;
import ai.runapi.core.errors.TaskTimeoutException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.Callable;

/** Polling utilities for synchronous {@code run()} methods. */
public final class Poller {
  private Poller() {}

  /** Polls until the fetched task completes, fails, or times out. */
  public static <T extends TaskResponse> T pollUntilComplete(
      Callable<T> fetcher, Duration interval, Duration maxWait) {
    return pollUntilComplete(fetcher, interval, maxWait, Clock.systemUTC(), Sleeper.SYSTEM);
  }

  static <T extends TaskResponse> T pollUntilComplete(
      Callable<T> fetcher, Duration interval, Duration maxWait, Clock clock, Sleeper sleeper) {
    Instant start = clock.instant();
    Instant deadline = start.plus(maxWait);
    T last = null;
    boolean first = true;
    while (true) {
      if (!first && !clock.instant().isBefore(deadline)) {
        throw new TaskTimeoutException("Task polling timed out", null);
      }
      first = false;
      try {
        last = fetcher.call();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      String normalized = normalizeResponseStatus(last);
      if ("completed".equals(normalized)) {
        ensureCompletedResult(last);
        return last;
      }
      if ("failed".equals(normalized)) {
        String message = last.getError() == null || last.getError().trim().isEmpty() ? "Task failed" : last.getError();
        throw new TaskFailedException(message, last);
      }
      // Clamp the sleep to the time left before the deadline so a long interval
      // cannot block well past maxWait before the timeout check fires.
      Duration remaining = Duration.between(clock.instant(), deadline);
      if (remaining.isZero() || remaining.isNegative()) {
        throw new TaskTimeoutException("Task polling timed out", null);
      }
      sleeper.sleep(interval.compareTo(remaining) < 0 ? interval : remaining);
    }
  }

  private static void ensureCompletedResult(TaskResponse response) {
    if (response instanceof CompletedResult) {
      ((CompletedResult) response).ensureResultPresent();
    }
  }

  /** Normalizes backend task status values for polling. */
  public static String normalize(TaskStatus status) {
    String value;
    try {
      value = status.value().toLowerCase(Locale.ROOT);
    } catch (RuntimeException e) {
      throw new ValidationException("Task response has an invalid status", 0, null, null, e);
    }
    if ("completed".equals(value) || "success".equals(value)) {
      return "completed";
    }
    if ("failed".equals(value)
        || "error".equals(value)
        || "generate_failed".equals(value)
        || "create_task_failed".equals(value)) {
      return "failed";
    }
    return "processing";
  }

  private static String normalizeResponseStatus(TaskResponse response) {
    try {
      return normalize(response.getStatus());
    } catch (ValidationException e) {
      throw e;
    } catch (RuntimeException e) {
      throw new ValidationException("Task response has an invalid status", 0, null, null, e);
    }
  }

  interface Sleeper {
    Sleeper SYSTEM =
        duration -> {
          try {
            Thread.sleep(duration.toMillis());
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
          }
        };

    void sleep(Duration duration);
  }

  /** Implemented by task responses whose completed result must contain a result field. */
  public interface CompletedResult {
    /** Throws when the completed response is missing its result field. */
    void ensureResultPresent();
  }
}
