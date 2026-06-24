package ai.runapi.core.errors;

/** Thrown when polling exceeds its configured maximum wait. */
public class TaskTimeoutException extends RunApiException {
  public TaskTimeoutException(String message, Throwable cause) {
    super(message, "task_timeout", 0, null, null, cause);
  }
}
