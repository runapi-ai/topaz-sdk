package ai.runapi.core.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Shared parameter-normalization helpers for SDK parameter types. The
 * logic is provider-agnostic, so it lives here once instead of being copied into
 * every model package's {@code *ParamUtils} class.
 */
public final class ParamSupport {
  private ParamSupport() {}

  // Required business/creative strings: reject null/blank but PRESERVE the
  // caller's value verbatim. Prompts and other creative content must not be
  // trimmed; only protocol identifiers are normalized via requireNonBlankTrim.
  public static String requireNonBlank(String value, String name) {
    if (Objects.requireNonNull(value, name).trim().isEmpty()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return value;
  }

  // Protocol identifiers (model / value-class strings): reject null/blank and
  // trim surrounding whitespace before use.
  public static String requireNonBlankTrim(String value, String name) {
    String checked = Objects.requireNonNull(value, name).trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return checked;
  }

  public static Map<String, Object> compact(Map<String, Object> raw) {
    Map<String, Object> compacted = new LinkedHashMap<String, Object>();
    for (Map.Entry<String, Object> entry : raw.entrySet()) {
      Object value = compactValue(entry.getValue());
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
    List<String> copy = new ArrayList<String>();
    for (String value : values) {
      copy.add(requireNonBlank(value, "value"));
    }
    return Collections.unmodifiableList(copy);
  }

  public static List<String> requiredStrings(List<String> values, String name) {
    List<String> copy = strings(Objects.requireNonNull(values, name));
    if (copy.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be empty");
    }
    return copy;
  }

  public static <T> List<T> list(List<T> values, String name) {
    if (values == null) {
      return null;
    }
    List<T> copy = new ArrayList<T>();
    for (T value : values) {
      copy.add(Objects.requireNonNull(value, name + " item"));
    }
    return Collections.unmodifiableList(copy);
  }

  public static <T> List<T> requiredList(List<T> values, String name) {
    List<T> copy = list(Objects.requireNonNull(values, name), name);
    if (copy.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be empty");
    }
    return copy;
  }

  public static List<Map<String, Object>> maps(List<Map<String, Object>> values, String name) {
    if (values == null) {
      return null;
    }
    List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> value : values) {
      copy.add(Collections.unmodifiableMap(new LinkedHashMap<String, Object>(Objects.requireNonNull(value, name + " item"))));
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

  private static Object compactValue(Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof String && ((String) value).trim().isEmpty()) {
      return null;
    }
    if (value instanceof List && ((List<?>) value).isEmpty()) {
      return null;
    }
    if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) {
      return null;
    }
    return value;
  }
}
