package ai.runapi.core.polling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** RunAPI-owned cost on a completed Task envelope. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TaskUsage {
  @JsonProperty("cost")
  private Double cost;

  /** Settled Task cost in USD. */
  public Double getCost() {
    return cost;
  }
}
