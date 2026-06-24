package ai.runapi.core.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/** Multipart form-data body that streams file parts from disk. */
public final class MultipartRequestBody implements RequestBody {
  private final String boundary;
  private final Map<String, String> fields;
  private final Map<String, FilePart> files;

  private MultipartRequestBody(Builder builder) {
    this.boundary = "runapi-" + UUID.randomUUID().toString();
    this.fields = Collections.unmodifiableMap(new LinkedHashMap<String, String>(builder.fields));
    this.files = Collections.unmodifiableMap(new LinkedHashMap<String, FilePart>(builder.files));
  }

  /** Creates a new multipart body builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the multipart content type with boundary. */
  @Override
  public String contentType() {
    return "multipart/form-data; boundary=" + boundary;
  }

  /** Returns {@code null}; multipart bodies stream file parts without precomputing length. */
  @Override
  public Long contentLength() {
    return null;
  }

  /** Returns {@code false}; file parts stream from disk. */
  @Override
  public boolean isRepeatable() {
    // File parts stream from disk via writeTo(); reporting non-repeatable keeps the
    // transport on its streaming path (getContent() pipes writeTo) instead of
    // buffering the entire upload into memory. File create/upload is POST and is not
    // retried by default, so giving up repeatability costs nothing here.
    return false;
  }

  /** Writes the multipart form-data body to the output stream. */
  @Override
  public void writeTo(OutputStream out) throws IOException {
    byte[] newline = "\r\n".getBytes("UTF-8");
    for (Map.Entry<String, String> field : fields.entrySet()) {
      writeHeaderLine(out, "--" + boundary + "\r\n");
      writeHeaderLine(out, "Content-Disposition: form-data; name=\"" + escape(field.getKey()) + "\"\r\n\r\n");
      out.write(field.getValue().getBytes("UTF-8"));
      out.write(newline);
    }
    for (Map.Entry<String, FilePart> entry : files.entrySet()) {
      FilePart file = entry.getValue();
      writeHeaderLine(out, "--" + boundary + "\r\n");
      writeHeaderLine(
          out,
          "Content-Disposition: form-data; name=\""
              + escape(entry.getKey())
              + "\"; filename=\""
              + escape(file.fileName)
              + "\"\r\n");
      if (file.contentType != null) {
        writeHeaderLine(out, "Content-Type: " + file.contentType + "\r\n");
      }
      writeHeaderLine(out, "\r\n");
      Files.copy(file.path, out);
      out.write(newline);
    }
    writeHeaderLine(out, "--" + boundary + "--\r\n");
  }

  private static void writeHeaderLine(OutputStream out, String value) throws IOException {
    // Header lines are UTF-8 encoded so non-ASCII part names / filenames (e.g.
    // "图片.png", "reçu.pdf") survive intact. RFC 7578 permits UTF-8 in the
    // Content-Disposition filename; US-ASCII would have replaced them with '?'.
    out.write(value.getBytes("UTF-8"));
  }

  private static String escape(String value) {
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  /** Builder for {@link MultipartRequestBody}. */
  public static final class Builder {
    private final Map<String, String> fields = new LinkedHashMap<String, String>();
    private final Map<String, FilePart> files = new LinkedHashMap<String, FilePart>();

    private Builder() {}

    /** Adds a string form field. */
    public Builder field(String name, String value) {
      fields.put(requireName(name), Objects.requireNonNull(value, "value"));
      return this;
    }

    /** Adds a file part. */
    public Builder file(String name, Path path, String fileName, @Nullable String contentType) {
      files.put(requireName(name), new FilePart(path, fileName, contentType));
      return this;
    }

    /** Builds a multipart body. */
    public MultipartRequestBody build() {
      return new MultipartRequestBody(this);
    }
  }

  private static String requireName(String value) {
    String checked = Objects.requireNonNull(value, "name").trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException("multipart part name must not be blank");
    }
    return checked;
  }

  private static final class FilePart {
    private final Path path;
    private final String fileName;
    private final @Nullable String contentType;

    private FilePart(Path path, String fileName, @Nullable String contentType) {
      this.path = Objects.requireNonNull(path, "path");
      String checked = Objects.requireNonNull(fileName, "fileName").trim();
      if (checked.isEmpty()) {
        throw new IllegalArgumentException("fileName must not be blank");
      }
      this.fileName = checked;
      this.contentType = contentType;
    }
  }
}
