package ai.runapi.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** One-shot stream request body for future streaming inputs. */
public final class StreamRequestBody implements RequestBody {
  private final InputStream input;
  private final @Nullable String contentType;
  private final @Nullable Long contentLength;
  private boolean written;

  public StreamRequestBody(
      InputStream input, @Nullable String contentType, @Nullable Long contentLength) {
    this.input = Objects.requireNonNull(input, "input");
    this.contentType = contentType;
    this.contentLength = contentLength;
  }

  /** Returns the content type, if known. */
  @Override
  public @Nullable String contentType() {
    return contentType;
  }

  /** Returns the content length, if known. */
  @Override
  public @Nullable Long contentLength() {
    return contentLength;
  }

  /** Returns {@code false}; the stream can only be consumed once. */
  @Override
  public boolean isRepeatable() {
    return false;
  }

  /** Streams the input bytes and closes the input stream. */
  @Override
  public void writeTo(OutputStream out) throws IOException {
    if (written) {
      throw new IOException("stream request body has already been written");
    }
    written = true;
    byte[] buffer = new byte[8192];
    int read;
    try {
      while ((read = input.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
    } finally {
      input.close();
    }
  }
}
