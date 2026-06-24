package ai.runapi.core.http;

import java.io.IOException;
import java.io.OutputStream;

/** Empty request body. */
public final class EmptyRequestBody implements RequestBody {
  private static final EmptyRequestBody INSTANCE = new EmptyRequestBody();

  private EmptyRequestBody() {}

  /** Returns the shared empty body instance. */
  public static EmptyRequestBody get() {
    return INSTANCE;
  }

  /** Returns {@code null} because the empty body has no content type. */
  @Override
  public String contentType() {
    return null;
  }

  /** Returns zero for the empty body. */
  @Override
  public Long contentLength() {
    return 0L;
  }

  /** Returns {@code true}; the empty body can always be sent again. */
  @Override
  public boolean isRepeatable() {
    return true;
  }

  /** Writes no bytes. */
  @Override
  public void writeTo(OutputStream out) throws IOException {
    // Intentionally empty.
  }
}
