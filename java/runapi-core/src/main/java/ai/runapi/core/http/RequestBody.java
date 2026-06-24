package ai.runapi.core.http;

import java.io.IOException;
import java.io.OutputStream;
import org.jspecify.annotations.Nullable;

/** Streaming request body abstraction used by SDK transports. */
public interface RequestBody {
  /** Content-Type header value, if known. */
  @Nullable String contentType();

  /** Content length, if known. */
  @Nullable Long contentLength();

  /** Whether the body can be written more than once. */
  boolean isRepeatable();

  /** Writes the body bytes to the output stream. */
  void writeTo(OutputStream out) throws IOException;
}
