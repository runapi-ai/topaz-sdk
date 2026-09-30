package ai.runapi.topaz.types;

import ai.runapi.core.types.ParamSupport;
import java.util.List;
import java.util.Map;

final class TopazParamUtils {
  private TopazParamUtils() {}

  static Map<String, Object> compact(Map<String, Object> raw) {
    return ParamSupport.compact(raw);
  }

  static List<String> strings(List<String> values) {
    return ParamSupport.strings(values);
  }

  static <T> List<T> list(List<T> values) {
    return ParamSupport.list(values);
  }

  static List<Map<String, Object>> maps(List<Map<String, Object>> values) {
    return ParamSupport.maps(values);
  }

  static Object wireValue(Object value) {
    return ParamSupport.wireValue(value);
  }
}
