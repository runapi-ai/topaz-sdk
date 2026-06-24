package ai.runapi.core.contract;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Contract metadata for one action. */
public final class ContractAction {
  private final List<String> models;
  private final Map<String, Map<String, ContractField>> fieldsByModel;
  private final Map<String, List<ContractRule>> rulesByModel;

  public ContractAction(List<String> models, Map<String, Map<String, ContractField>> fieldsByModel) {
    this(models, fieldsByModel, Collections.<String, List<ContractRule>>emptyMap());
  }

  public ContractAction(
      List<String> models,
      Map<String, Map<String, ContractField>> fieldsByModel,
      Map<String, List<ContractRule>> rulesByModel) {
    this.models = Collections.unmodifiableList(models);
    this.fieldsByModel =
        Collections.unmodifiableMap(
            new LinkedHashMap<String, Map<String, ContractField>>(fieldsByModel));
    this.rulesByModel =
        Collections.unmodifiableMap(
            new LinkedHashMap<String, List<ContractRule>>(rulesByModel));
  }

  /** Allowed model slugs for this action. */
  public List<String> getModels() {
    return models;
  }

  /** Field constraints grouped by model slug. */
  public Map<String, Map<String, ContractField>> getFieldsByModel() {
    return fieldsByModel;
  }

  /** Cross-field rules grouped by model slug. */
  public Map<String, List<ContractRule>> getRulesByModel() {
    return rulesByModel;
  }
}
