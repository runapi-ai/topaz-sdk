package ai.runapi.core.account;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Account details associated with the current API key. */
public final class AccountRecord {
  @JsonProperty("id")
  private long id;

  @JsonProperty("name")
  private String name;
  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** Account ID. */
  public long getId() {
    return id;
  }

  /** Account name. */
  public String getName() {
    return name;
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
