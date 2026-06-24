package ai.runapi.core.http;

import ai.runapi.core.json.Json;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/** JSON request body encoded with the SDK ObjectMapper. */
public final class JsonRequestBody implements RequestBody {
  private final Object value;

  public JsonRequestBody(Object value) {
    this.value = Objects.requireNonNull(value, "value");
  }

  /** Returns the JSON content type. */
  @Override
  public String contentType() {
    return "application/json";
  }

  /** Returns {@code null}; JSON bodies are streamed without precomputing length. */
  @Override
  public Long contentLength() {
    return null;
  }

  /** Returns {@code true}; the JSON value can be serialized again. */
  @Override
  public boolean isRepeatable() {
    return true;
  }

  /** Serializes the JSON value to the output stream. */
  @Override
  public void writeTo(OutputStream out) throws IOException {
    Json.mapper().writeValue(out, value);
  }
}
