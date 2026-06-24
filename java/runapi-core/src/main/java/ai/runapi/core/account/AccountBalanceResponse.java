package ai.runapi.core.account;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Current account balance and usage totals. */
public final class AccountBalanceResponse {
  @JsonProperty("balance_cents")
  private long balanceCents;

  @JsonProperty("paid_balance_cents")
  private long paidBalanceCents;

  @JsonProperty("bonus_balance_cents")
  private long bonusBalanceCents;

  @JsonProperty("spent_cents_today")
  private long spentCentsToday;

  @JsonProperty("spent_cents_total")
  private long spentCentsTotal;

  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** Remaining balance. */
  public long getBalanceCents() {
    return balanceCents;
  }

  /** Remaining paid balance. */
  public long getPaidBalanceCents() {
    return paidBalanceCents;
  }

  /** Remaining bonus balance. */
  public long getBonusBalanceCents() {
    return bonusBalanceCents;
  }

  /** Amount spent today. */
  public long getSpentCentsToday() {
    return spentCentsToday;
  }

  /** Lifetime amount spent. */
  public long getSpentCentsTotal() {
    return spentCentsTotal;
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
