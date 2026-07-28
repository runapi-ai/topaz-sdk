package ai.runapi.core.polling;

import ai.runapi.core.billing.TaskBillingFacts;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Response returned after creating an asynchronous task. */
public final class TaskCreateResponse {
  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private String status;

  @JsonProperty("task_replayed")
  private Boolean taskReplayed;

  @JsonProperty("billing")
  private TaskBillingFacts billing;

  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** Task identifier. */
  public String getId() {
    return id;
  }

  /** Initial task status, when returned. */
  public String getStatus() {
    return status;
  }

  /** Whether this idempotency key reused the original task, when returned. */
  public Boolean getTaskReplayed() {
    return taskReplayed;
  }

  /** Persisted billing facts recorded when the task was accepted. */
  public TaskBillingFacts getBilling() {
    return billing;
  }

  /** Unknown response fields preserved as JSON nodes. */
  public Map<String, JsonNode> extraFields() {
    return Collections.unmodifiableMap(extraFields);
  }

  @JsonAnySetter
  void putExtraField(String name, JsonNode value) {
    extraFields.put(name, value);
  }
}
