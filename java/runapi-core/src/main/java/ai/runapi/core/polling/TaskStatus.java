package ai.runapi.core.polling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Open task status value class. */
public final class TaskStatus {
  /** Completed task status. */
  public static final TaskStatus COMPLETED = new TaskStatus("completed");
  /** Success task status, normalized as completed by polling. */
  public static final TaskStatus SUCCESS = new TaskStatus("success");
  /** Failed task status. */
  public static final TaskStatus FAILED = new TaskStatus("failed");
  /** Error task status, normalized as failed by polling. */
  public static final TaskStatus ERROR = new TaskStatus("error");
  /** Generate-failed task status, normalized as failed by polling. */
  public static final TaskStatus GENERATE_FAILED = new TaskStatus("generate_failed");
  /** Create-task-failed task status, normalized as failed by polling. */
  public static final TaskStatus CREATE_TASK_FAILED = new TaskStatus("create_task_failed");
  /** Processing task status. */
  public static final TaskStatus PROCESSING = new TaskStatus("processing");
  /** Pending task status. */
  public static final TaskStatus PENDING = new TaskStatus("pending");

  private final String value;

  @JsonCreator
  public TaskStatus(String value) {
    String checked = Objects.requireNonNull(value, "value").trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException("value must not be blank");
    }
    this.value = checked;
  }

  /** Raw status value. */
  @JsonValue
  public String value() {
    return value;
  }

  /** Returns the raw task status value. */
  @Override
  public String toString() {
    return value;
  }

  /** Compares task status values by raw value. */
  @Override
  public boolean equals(Object other) {
    return other instanceof TaskStatus && value.equals(((TaskStatus) other).value);
  }

  /** Returns a hash code for the raw status value. */
  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
