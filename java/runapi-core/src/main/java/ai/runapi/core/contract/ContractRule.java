package ai.runapi.core.contract;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Cross-field input rule metadata. */
public final class ContractRule {
  private final Map<String, Object> conditions;
  private final List<String> required;
  private final List<String> requiredAny;
  private final List<String> forbidden;
  private final Map<String, List<Object>> narrowedEnums;

  ContractRule(Map<String, Object> conditions, List<String> required, List<String> forbidden) {
    this(
        conditions,
        required,
        Collections.<String>emptyList(),
        forbidden,
        Collections.<String, List<Object>>emptyMap());
  }

  ContractRule(
      Map<String, Object> conditions,
      List<String> required,
      List<String> requiredAny,
      List<String> forbidden,
      Map<String, List<Object>> narrowedEnums) {
    this.conditions =
        Collections.unmodifiableMap(new LinkedHashMap<String, Object>(conditions));
    this.required = Collections.unmodifiableList(required);
    this.requiredAny = Collections.unmodifiableList(requiredAny);
    this.forbidden = Collections.unmodifiableList(forbidden);
    this.narrowedEnums =
        Collections.unmodifiableMap(new LinkedHashMap<String, List<Object>>(narrowedEnums));
  }

  /**
   * Conditions that activate this rule. A value is either a scalar the field's
   * effective value must equal, or a single-entry map {@code {present=Boolean}}
   * branching on whether the caller supplied the field at all.
   */
  public Map<String, Object> getConditions() {
    return conditions;
  }

  /** Fields required when the rule is active. */
  public List<String> getRequired() {
    return required;
  }

  /** Fields of which at least one is required when the rule is active. */
  public List<String> getRequiredAny() {
    return requiredAny;
  }

  /** Fields forbidden when the rule is active. */
  public List<String> getForbidden() {
    return forbidden;
  }

  /** Per-field enums narrowed while the rule is active. */
  public Map<String, List<Object>> getNarrowedEnums() {
    return narrowedEnums;
  }
}
