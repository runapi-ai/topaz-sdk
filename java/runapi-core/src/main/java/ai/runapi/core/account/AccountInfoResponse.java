package ai.runapi.core.account;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Profile and account details for the authenticated API key. */
public final class AccountInfoResponse {
  @JsonProperty("id")
  private long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("email")
  private String email;

  @JsonProperty("account")
  private AccountRecord account;
  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** User ID. */
  public long getId() {
    return id;
  }

  /** User name. */
  public String getName() {
    return name;
  }

  /** User email. */
  public String getEmail() {
    return email;
  }

  /** Account associated with the API key. */
  public AccountRecord getAccount() {
    return account;
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
