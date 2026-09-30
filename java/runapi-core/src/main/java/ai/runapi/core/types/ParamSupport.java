package ai.runapi.core.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared parameter-normalization helpers for SDK parameter types. The
 * logic is provider-agnostic, so it lives here once instead of being copied into
 * every model package's {@code *ParamUtils} class.
 */
public final class ParamSupport {
  private ParamSupport() {}

  public static Map<String, Object> compact(Map<String, Object> raw) {
    Map<String, Object> compacted = new LinkedHashMap<String, Object>();
    for (Map.Entry<String, Object> entry : raw.entrySet()) {
      Object value = entry.getValue();
      if (value != null) {
        compacted.put(entry.getKey(), value);
      }
    }
    return Collections.unmodifiableMap(compacted);
  }

  public static List<String> strings(List<String> values) {
    if (values == null) {
      return null;
    }
    return Collections.unmodifiableList(new ArrayList<String>(values));
  }

  public static <T> List<T> list(List<T> values) {
    if (values == null) {
      return null;
    }
    return Collections.unmodifiableList(new ArrayList<T>(values));
  }

  public static List<Map<String, Object>> maps(List<Map<String, Object>> values) {
    if (values == null) {
      return null;
    }
    List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> value : values) {
      copy.add(value == null ? null : Collections.unmodifiableMap(new LinkedHashMap<String, Object>(value)));
    }
    return Collections.unmodifiableList(copy);
  }

  /** Unwraps a {@link RunApiValue} to its raw wire string; other values pass through. */
  public static Object wireValue(Object value) {
    if (value instanceof RunApiValue) {
      return ((RunApiValue) value).value();
    }
    return value;
  }
}
