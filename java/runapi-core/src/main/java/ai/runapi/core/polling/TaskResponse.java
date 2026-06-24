package ai.runapi.core.polling;

import org.jspecify.annotations.Nullable;

/** Minimal task response contract used by the poller. */
public interface TaskResponse {
  /** Task identifier. */
  String getId();

  /** Current task status. */
  TaskStatus getStatus();

  /** Error message when the task failed. */
  @Nullable String getError();
}
