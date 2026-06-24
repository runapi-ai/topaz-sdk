package ai.runapi.core.contract;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Helper methods used by contract metadata. */
final class ContractBuilders {
  private ContractBuilders() {}

  static List<String> list(String... values) {
    return Collections.unmodifiableList(Arrays.asList(values));
  }

  static FieldPart required() {
    return field -> new ContractField(true, field.getEnumValues(), field.getMin(), field.getMax(), field.isLength());
  }

  static FieldPart enumValues(Object... values) {
    final List<Object> enumValues = Collections.unmodifiableList(Arrays.asList(values));
    return field -> new ContractField(field.isRequired(), enumValues, field.getMin(), field.getMax(), field.isLength());
  }

  static FieldPart min(final Double value) {
    return field -> new ContractField(field.isRequired(), field.getEnumValues(), value, field.getMax(), field.isLength());
  }

  static FieldPart max(final Double value) {
    return field -> new ContractField(field.isRequired(), field.getEnumValues(), field.getMin(), value, field.isLength());
  }

  static FieldPart length() {
    return field -> new ContractField(field.isRequired(), field.getEnumValues(), field.getMin(), field.getMax(), true);
  }

  static ContractField field(FieldPart... parts) {
    ContractField field = new ContractField(false, Collections.emptyList(), null, null, false);
    for (FieldPart part : parts) {
      field = part.apply(field);
    }
    return field;
  }

  static Map<String, ContractField> fields(Object[][] entries) {
    Map<String, ContractField> fields = new LinkedHashMap<String, ContractField>();
    for (Object[] entry : entries) {
      fields.put((String) entry[0], (ContractField) entry[1]);
    }
    return Collections.unmodifiableMap(fields);
  }

  @SuppressWarnings("unchecked")
  static Map<String, Map<String, ContractField>> fieldsByModel(Object[][] entries) {
    Map<String, Map<String, ContractField>> fields = new LinkedHashMap<String, Map<String, ContractField>>();
    for (Object[] entry : entries) {
      fields.put((String) entry[0], (Map<String, ContractField>) entry[1]);
    }
    return Collections.unmodifiableMap(fields);
  }

  static Map<String, Object> conditions(Object[][] entries) {
    Map<String, Object> conditions = new LinkedHashMap<String, Object>();
    for (Object[] entry : entries) {
      conditions.put((String) entry[0], entry[1]);
    }
    return Collections.unmodifiableMap(conditions);
  }

  static ContractRule rule(Map<String, Object> conditions, List<String> required, List<String> forbidden) {
    return new ContractRule(conditions, required, forbidden);
  }

  static List<ContractRule> rules(ContractRule... values) {
    return Collections.unmodifiableList(Arrays.asList(values));
  }

  @SuppressWarnings("unchecked")
  static Map<String, List<ContractRule>> rulesByModel(Object[][] entries) {
    Map<String, List<ContractRule>> rules = new LinkedHashMap<String, List<ContractRule>>();
    for (Object[] entry : entries) {
      rules.put((String) entry[0], (List<ContractRule>) entry[1]);
    }
    return Collections.unmodifiableMap(rules);
  }

  interface FieldPart {
    ContractField apply(ContractField field);
  }
}
