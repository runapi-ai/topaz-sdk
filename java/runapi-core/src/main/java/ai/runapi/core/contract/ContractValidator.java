package ai.runapi.core.contract;

import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.types.ParamSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Strict input validator backed by SDK contract metadata. */
public final class ContractValidator {
  private ContractValidator() {}

  /**
   * Validates params for the given action. Params built by an SDK retain their
   * pre-compaction values for validation while exposing only the compact wire shape.
   */
  public static void validate(String action, Map<String, Object> params) {
    ContractAction contract = ContractGen.contract().get(action);
    if (contract == null) {
      throw new ValidationException("Unknown action: " + action);
    }
    validate(contract, params);
  }

  static void validate(ContractAction contract, Map<String, Object> params) {
    params = ParamSupport.validationInput(params);
    String selectedModel = null;
    Map<String, ContractField> fields;
    if (contract.getModels().isEmpty()) {
      fields = contract.getFieldsByModel().get("_");
      selectedModel = "_";
    } else {
      Object modelValue = params.get("model");
      if (isBlank(modelValue) && contract.getModels().size() == 1) {
        selectedModel = contract.getModels().get(0);
        fields = contract.getFieldsByModel().get(selectedModel);
      } else if (isBlank(modelValue)) {
        throw new ValidationException("model is required");
      } else {
        String model = String.valueOf(modelValue);
        if (!contract.getModels().contains(model)) {
          throw new ValidationException("model must be one of: " + joinSorted(contract.getModels()));
        }
        selectedModel = model;
        fields = contract.getFieldsByModel().get(model);
      }
    }
    if (selectedModel != null) {
      validateRules(contract.getRulesByModel().get(selectedModel), params, selectedModel);
    }
    if (fields != null) {
      for (Map.Entry<String, ContractField> entry : fields.entrySet()) {
        validateField(entry.getKey(), entry.getValue(), params.get(entry.getKey()));
      }
    }
  }

  private static void validateField(String name, ContractField field, Object value) {
    if (value != null && (field.getMinItems() != null || field.getMaxItems() != null)) {
      validateItemCount(name, value, field);
    }
    if (field.isRequired() && isBlank(value)) {
      throw new ValidationException(name + " is required");
    }
    if (isBlank(value)) {
      return;
    }
    if (!field.getEnumValues().isEmpty() && !containsEnumValue(field.getEnumValues(), value)) {
      throw new ValidationException(name + " must be one of: " + join(field.getEnumValues()));
    }
    if (field.isLength()) {
      // Measure the string form in Unicode codepoints (matches the rune/codepoint
      // counting the other SDKs use); typed builders always pass a String here.
      String text = String.valueOf(value);
      checkRange(name, text.codePointCount(0, text.length()), field, "characters");
    } else if (field.getMin() != null || field.getMax() != null) {
      if (!(value instanceof Number)) {
        throw new ValidationException(name + " must be a number");
      }
      checkRange(name, ((Number) value).doubleValue(), field, "");
    }
  }

  private static void validateItemCount(String name, Object value, ContractField field) {
    if (!(value instanceof List)) {
      throw new ValidationException(name + " must be an array");
    }

    int count = ((List<?>) value).size();
    Integer min = field.getMinItems();
    Integer max = field.getMaxItems();
    if ((min == null || count >= min) && (max == null || count <= max)) {
      return;
    }
    throw new ValidationException(itemCountMessage(name, min, max));
  }

  private static String itemCountMessage(String name, Integer min, Integer max) {
    if (min != null && max != null) {
      return name + " must contain between " + min + " and " + max + " items";
    }
    if (min != null) {
      return name + " must contain at least " + min + " items";
    }
    return name + " must contain at most " + max + " items";
  }

  private static void checkRange(String name, double measured, ContractField field, String unit) {
    Double min = field.getMin();
    Double max = field.getMax();
    boolean belowMin = min != null && measured < min;
    boolean aboveMax = max != null && measured > max;
    if (belowMin || aboveMax) {
      throw new ValidationException(rangeMessage(name, min, max, unit));
    }
  }

  // Mirrors the gateway / Go / JS / Ruby / Python range messages exactly:
  // "X must be between A and B", "X must be at least A", "X must be at most B",
  // with a " characters" suffix for length-constrained fields.
  private static String rangeMessage(String name, Double min, Double max, String unit) {
    String suffix = unit.isEmpty() ? "" : " " + unit;
    if (min != null && max != null) {
      return name + " must be between " + formatNumber(min) + " and " + formatNumber(max) + suffix;
    }
    if (min != null) {
      return name + " must be at least " + formatNumber(min) + suffix;
    }
    return name + " must be at most " + formatNumber(max) + suffix;
  }

  private static void validateRules(
      List<ContractRule> rules, Map<String, Object> params, String selectedModel) {
    if (rules == null || rules.isEmpty()) {
      return;
    }
    for (ContractRule rule : rules) {
      if (!conditionsMet(rule.getConditions(), params, selectedModel)) {
        continue;
      }
      String context = conditionDescription(rule.getConditions());
      for (String field : rule.getRequired()) {
        if (isBlank(params.get(field))) {
          throw new ValidationException(field + " is required when " + context);
        }
      }
      for (String field : rule.getForbidden()) {
        if (!isBlank(params.get(field))) {
          throw new ValidationException(field + " is not allowed when " + context);
        }
      }
    }
  }

  private static boolean conditionsMet(
      Map<String, Object> conditions, Map<String, Object> params, String selectedModel) {
    for (Map.Entry<String, Object> entry : conditions.entrySet()) {
      Object value = params.get(entry.getKey());
      if (value == null && "model".equals(entry.getKey())) {
        value = selectedModel;
      }
      if (value == null || !String.valueOf(value).equals(String.valueOf(entry.getValue()))) {
        return false;
      }
    }
    return true;
  }

  private static String conditionDescription(Map<String, Object> conditions) {
    // Sort condition keys so multi-condition rule messages are identical across SDKs
    // (Go/JS/Python all sort), not dependent on map iteration order.
    List<String> keys = new ArrayList<String>(conditions.keySet());
    Collections.sort(keys);
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < keys.size(); i++) {
      if (i > 0) {
        builder.append(" and ");
      }
      String key = keys.get(i);
      builder.append(key).append(" is ").append(conditions.get(key));
    }
    return builder.toString();
  }

  private static boolean containsEnumValue(List<Object> allowed, Object value) {
    for (Object item : allowed) {
      if (String.valueOf(item).equals(String.valueOf(value))) {
        return true;
      }
    }
    return false;
  }

  private static boolean isBlank(Object value) {
    if (value == null) {
      return true;
    }
    if (value instanceof String) {
      return ((String) value).trim().isEmpty();
    }
    if (value instanceof List) {
      return ((List<?>) value).isEmpty();
    }
    return false;
  }

  private static String joinSorted(List<String> values) {
    List<String> sorted = new ArrayList<String>(values);
    Collections.sort(sorted);
    return join(sorted);
  }

  private static String join(List<?> values) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < values.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(values.get(i));
    }
    return builder.toString();
  }

  private static String formatNumber(Double value) {
    if (value.doubleValue() == value.longValue()) {
      return Long.toString(value.longValue());
    }
    return value.toString();
  }
}
