package ai.runapi.topaz.types;

import ai.runapi.core.types.RunApiValue;

abstract class TopazValue extends RunApiValue {
  TopazValue(String value) {
    super(value);
  }
}
