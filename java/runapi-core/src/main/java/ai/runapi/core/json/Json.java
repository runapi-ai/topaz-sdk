package ai.runapi.core.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** Shared Jackson configuration for SDK JSON encoding and decoding. */
public final class Json {
  private static final ObjectMapper MAPPER = create();

  private Json() {}

  /** Returns the shared ObjectMapper. */
  public static ObjectMapper mapper() {
    return MAPPER;
  }

  private static ObjectMapper create() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    return mapper;
  }
}
