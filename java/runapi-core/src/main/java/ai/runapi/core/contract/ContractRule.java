package ai.runapi.core.contract;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Cross-field input rule metadata. */
public final class ContractRule {
  private final Map<String, Object> conditions;
  private final List<String> required;
  private final List<String> forbidden;

  ContractRule(Map<String, Object> conditions, List<String> required, List<String> forbidden) {
    this.conditions =
        Collections.unmodifiableMap(new LinkedHashMap<String, Object>(conditions));
    this.required = Collections.unmodifiableList(required);
    this.forbidden = Collections.unmodifiableList(forbidden);
  }

  /** Conditions that activate this rule. */
  public Map<String, Object> getConditions() {
    return conditions;
  }

  /** Fields required when the rule is active. */
  public List<String> getRequired() {
    return required;
  }

  /** Fields forbidden when the rule is active. */
  public List<String> getForbidden() {
    return forbidden;
  }
}
