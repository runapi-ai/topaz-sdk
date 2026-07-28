package ai.runapi.core.polling;

import ai.runapi.core.billing.TaskBillingFacts;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/** Base class for SDK task responses. */
public abstract class AbstractTaskResponse implements TaskResponse {
  @JsonProperty("billing")
  private TaskBillingFacts billing;

  /** Persisted billing facts for this task, when supplied by the API. */
  public TaskBillingFacts getBilling() {
    return billing;
  }
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
