package ai.runapi.core.types;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/**
 * Base for scalar value-object types (model ids, enums) that serialize as their
 * raw string. Shared by every model package so the normalization and
 * equality semantics live in one place instead of being copied per module.
 */
public abstract class RunApiValue {
  private final String value;

  protected RunApiValue(String value) {
    String checked = Objects.requireNonNull(value, "value").trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException("value must not be blank");
    }
    this.value = checked;
  }

  /** Returns the raw string value sent to the RunAPI contract. */
  @JsonValue
  public final String value() {
    return value;
  }

  /** Returns the raw string value. */
  @Override
  public final String toString() {
    return value;
  }

  /** Compares values by concrete type and raw value. */
  @Override
  public final boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    return value.equals(((RunApiValue) other).value);
  }

  /** Returns a hash code for the raw value. */
  @Override
  public final int hashCode() {
    return value.hashCode();
  }
}
