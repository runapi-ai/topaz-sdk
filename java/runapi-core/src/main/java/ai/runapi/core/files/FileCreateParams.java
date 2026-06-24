package ai.runapi.core.files;

import ai.runapi.core.RequestOptions;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Parameters for creating a temporary RunAPI file. */
public final class FileCreateParams {
  private final @Nullable Path path;
  private final @Nullable Source source;
  private final @Nullable String fileName;

  private FileCreateParams(@Nullable Path path, @Nullable Source source, @Nullable String fileName) {
    this.path = path;
    this.source = source;
    this.fileName = fileName;
    validateExactlyOneSource();
  }

  /** Creates params for streaming a local file as multipart form data. */
  public static Builder fromPath(Path path) {
    return new Builder(Objects.requireNonNull(path, "path"), null);
  }

  /** Creates params for importing a file from a remote HTTPS URL. */
  public static Builder fromUrl(String url) {
    return new Builder(null, Source.url(url));
  }

  /** Creates params for importing inline base64 data. */
  public static Builder fromBase64(String data) {
    return new Builder(null, Source.base64(data));
  }

  @Nullable Path getPath() {
    return path;
  }

  @Nullable Source getSource() {
    return source;
  }

  @Nullable String getFileName() {
    return fileName;
  }

  Map<String, Object> toJsonBody() {
    Source checked = Objects.requireNonNull(source, "source");
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("source", checked.toMap());
    if (fileName != null) {
      body.put("file_name", fileName);
    }
    return Collections.unmodifiableMap(body);
  }

  String multipartFileName() {
    if (fileName != null) {
      return fileName;
    }
    Path checked = Objects.requireNonNull(path, "path");
    Path name = checked.getFileName();
    return name == null ? "file" : name.toString();
  }

  private void validateExactlyOneSource() {
    int count = 0;
    if (path != null) {
      count++;
    }
    if (source != null) {
      count++;
    }
    if (count != 1) {
      throw new IllegalArgumentException("Exactly one source is required: path, url, or base64");
    }
  }

  private static String requireNonBlank(String value, String name) {
    String checked = Objects.requireNonNull(value, name).trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return checked;
  }

  /** Builder for {@link FileCreateParams}. */
  public static final class Builder {
    private final @Nullable Path path;
    private final @Nullable Source source;
    private @Nullable String fileName;

    private Builder(@Nullable Path path, @Nullable Source source) {
      this.path = path;
      this.source = source;
    }

    /** Sets an explicit file name. */
    public Builder fileName(String value) {
      this.fileName = requireNonBlank(value, "fileName");
      return this;
    }

    /** Builds immutable file-create parameters. */
    public FileCreateParams build() {
      return new FileCreateParams(path, source, fileName);
    }
  }

  private static final class Source {
    private final String type;
    private final @Nullable String url;
    private final @Nullable String data;

    private Source(String type, @Nullable String url, @Nullable String data) {
      this.type = type;
      this.url = url;
      this.data = data;
    }

    private static Source url(String value) {
      return new Source("url", requireNonBlank(value, "url"), null);
    }

    private static Source base64(String value) {
      return new Source("base64", null, requireNonBlank(value, "data"));
    }

    private Map<String, Object> toMap() {
      Map<String, Object> source = new LinkedHashMap<String, Object>();
      source.put("type", type);
      if (url != null) {
        source.put("url", url);
      }
      if (data != null) {
        source.put("data", data);
      }
      return Collections.unmodifiableMap(source);
    }
  }
}
