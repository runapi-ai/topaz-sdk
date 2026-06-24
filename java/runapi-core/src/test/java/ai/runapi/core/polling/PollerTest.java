package ai.runapi.core.polling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.errors.TaskFailedException;
import ai.runapi.core.errors.TaskTimeoutException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class PollerTest {
  @Test
  void normalizesStatuses() {
    assertEquals("completed", Poller.normalize(new TaskStatus("success")));
    assertEquals("failed", Poller.normalize(new TaskStatus("generate_failed")));
    assertEquals("processing", Poller.normalize(new TaskStatus("queued")));
  }

  @Test
  void returnsCompletedResponse() {
    AtomicInteger calls = new AtomicInteger();
    MutableClock clock = new MutableClock();

    TestTask result =
        Poller.pollUntilComplete(
            () ->
                calls.incrementAndGet() == 1
                    ? new TestTask("1", "processing", null)
                    : new TestTask("1", "completed", null),
            Duration.ofMillis(1),
            Duration.ofSeconds(1),
            clock,
            duration -> clock.advance(duration));

    assertEquals("completed", result.getStatus().value());
  }

  @Test
  void throwsOnFailureAndKeepsResponse() {
    TaskFailedException error =
        assertThrows(
            TaskFailedException.class,
            () ->
                Poller.pollUntilComplete(
                    () -> new TestTask("1", "failed", "bad"),
                    Duration.ofMillis(1),
                    Duration.ofSeconds(1),
                    new MutableClock(),
                    duration -> {}));

    assertEquals("bad", error.getMessage());
    assertInstanceOf(TestTask.class, error.getTaskResponse());
  }

  @Test
  void timesOut() {
    MutableClock clock = new MutableClock();

    assertThrows(
        TaskTimeoutException.class,
        () ->
            Poller.pollUntilComplete(
                () -> new TestTask("1", "processing", null),
                Duration.ofMillis(10),
                Duration.ofMillis(5),
                clock,
                duration -> clock.advance(duration)));
  }

  @Test
  void doesNotFetchAgainAfterMaxWaitHasElapsed() {
    MutableClock clock = new MutableClock();
    AtomicInteger calls = new AtomicInteger();

    assertThrows(
        TaskTimeoutException.class,
        () ->
            Poller.pollUntilComplete(
                () -> {
                  calls.incrementAndGet();
                  return new TestTask("1", "processing", null);
                },
                Duration.ofMillis(10),
                Duration.ofMillis(5),
                clock,
                duration -> clock.advance(duration)));

    assertEquals(1, calls.get());
  }

  @Test
  void clampsSleepSoTotalWaitDoesNotExceedMaxWait() {
    MutableClock clock = new MutableClock();
    AtomicLong slept = new AtomicLong();

    assertThrows(
        TaskTimeoutException.class,
        () ->
            Poller.pollUntilComplete(
                () -> new TestTask("1", "processing", null),
                Duration.ofSeconds(30),
                Duration.ofSeconds(35),
                clock,
                duration -> {
                  slept.addAndGet(duration.toMillis());
                  clock.advance(duration);
                }));

    // Without clamping, two full 30s sleeps would elapse (60s) before the timeout
    // check fires, overshooting the 35s maxWait. Clamped, total sleep <= maxWait.
    assertTrue(slept.get() <= Duration.ofSeconds(35).toMillis());
  }

  @Test
  void convertsMalformedStatusToValidationException() {
    ValidationException error =
        assertThrows(
            ValidationException.class,
            () ->
                Poller.pollUntilComplete(
                    MalformedStatusTask::new,
                    Duration.ofMillis(1),
                    Duration.ofMillis(5),
                    new MutableClock(),
                    duration -> {}));

    assertEquals("Task response has an invalid status", error.getMessage());
  }

  private static final class TestTask implements TaskResponse {
    private final String id;
    private final TaskStatus status;
    private final String error;

    private TestTask(String id, String status, String error) {
      this.id = id;
      this.status = new TaskStatus(status);
      this.error = error;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public TaskStatus getStatus() {
      return status;
    }

    @Override
    public String getError() {
      return error;
    }
  }

  private static final class MutableClock extends Clock {
    private Instant current = Instant.EPOCH;

    void advance(Duration duration) {
      current = current.plus(duration);
    }

    @Override
    public ZoneOffset getZone() {
      return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(java.time.ZoneId zone) {
      return this;
    }

    @Override
    public Instant instant() {
      return current;
    }
  }

  private static final class MalformedStatusTask implements TaskResponse {
    @Override
    public String getId() {
      return "1";
    }

    @Override
    public TaskStatus getStatus() {
      throw new IllegalArgumentException("value must not be blank");
    }

    @Override
    public String getError() {
      return null;
    }
  }
}
