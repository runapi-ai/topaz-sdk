package ai.runapi.core.polling;

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

  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** Task identifier. */
  public String getId() {
    return id;
  }

  /** Initial task status, when returned. */
  public String getStatus() {
    return status;
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
