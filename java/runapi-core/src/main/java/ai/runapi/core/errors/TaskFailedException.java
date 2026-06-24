package ai.runapi.core.errors;

import org.jspecify.annotations.Nullable;

/** Thrown when an async task fails during polling. */
public class TaskFailedException extends RunApiException {
  private final @Nullable Object taskResponse;

  public TaskFailedException(String message, @Nullable Object taskResponse) {
    super(message, "task_failed", 0, null, null, null);
    this.taskResponse = taskResponse;
  }

  /** Final task response that caused the failure, if available. */
  public @Nullable Object getTaskResponse() {
    return taskResponse;
  }
}
