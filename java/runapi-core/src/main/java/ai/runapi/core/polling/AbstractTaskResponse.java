package ai.runapi.core.polling;

import org.jspecify.annotations.Nullable;

/** Base class for SDK task responses. */
public abstract class AbstractTaskResponse implements TaskResponse {
  /** Returns the task ID. */
  @Override
  public abstract String getId();

  /** Returns the current task status. */
  @Override
  public abstract TaskStatus getStatus();

  /** Returns the task error message, if the task failed. */
  @Override
  public abstract @Nullable String getError();
}
