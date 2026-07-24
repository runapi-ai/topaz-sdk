package ai.runapi.core.contract;

import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.Nullable;

/** Field-level contract metadata. */
public final class ContractField {
  private final boolean required;
  private final List<Object> enumValues;
  private final @Nullable Double min;
  private final @Nullable Double max;
  private final boolean length;
  private final @Nullable Integer minItems;
  private final @Nullable Integer maxItems;

  ContractField(
      boolean required,
      List<Object> enumValues,
      @Nullable Double min,
      @Nullable Double max,
      boolean length,
      @Nullable Integer minItems,
      @Nullable Integer maxItems) {
    this.required = required;
    this.enumValues = Collections.unmodifiableList(enumValues);
    this.min = min;
    this.max = max;
    this.length = length;
    this.minItems = minItems;
    this.maxItems = maxItems;
  }

  /** Whether this field is required for the selected model. */
  public boolean isRequired() {
    return required;
  }

  /** Allowed enum values, or an empty list when unconstrained. */
  public List<Object> getEnumValues() {
    return enumValues;
  }

  /** Minimum numeric value, if set. */
  public @Nullable Double getMin() {
    return min;
  }

  /** Maximum numeric value, if set. */
  public @Nullable Double getMax() {
    return max;
  }

  /** Whether min/max constrain string length instead of numeric value. */
  public boolean isLength() {
    return length;
  }

  /** Minimum array item count, if set. */
  public @Nullable Integer getMinItems() {
    return minItems;
  }

  /** Maximum array item count, if set. */
  public @Nullable Integer getMaxItems() {
    return maxItems;
  }
}
