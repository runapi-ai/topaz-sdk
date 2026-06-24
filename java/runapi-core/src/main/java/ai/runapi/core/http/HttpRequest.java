package ai.runapi.core.http;

import ai.runapi.core.RequestOptions;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Immutable HTTP request passed to SDK transports. */
public final class HttpRequest {
  private final HttpMethod method;
  private final String path;
  private final Map<String, String> query;
  private final Map<String, String> headers;
  private final @Nullable RequestBody body;
  private final RequestOptions options;

  private HttpRequest(Builder builder) {
    this.method = Objects.requireNonNull(builder.method, "method");
    this.path = requireNonBlank(builder.path, "path");
    this.query = Collections.unmodifiableMap(new LinkedHashMap<String, String>(builder.query));
    this.headers = Collections.unmodifiableMap(new LinkedHashMap<String, String>(builder.headers));
    this.body = builder.body;
    this.options = builder.options;
  }

  /** Creates a new request builder. */
  public static Builder builder(HttpMethod method, String path) {
    return new Builder(method, path);
  }

  /** Returns the HTTP method. */
  public HttpMethod getMethod() {
    return method;
  }

  /** Returns the request path relative to the configured base URL. */
  public String getPath() {
    return path;
  }

  /** Returns immutable query parameters. */
  public Map<String, String> getQuery() {
    return query;
  }

  /** Returns immutable custom headers. */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /** Returns the request body, if present. */
  public @Nullable RequestBody getBody() {
    return body;
  }

  /** Returns per-request options. */
  public RequestOptions getOptions() {
    return options;
  }

  /** Builder for {@link HttpRequest}. */
  public static final class Builder {
    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query = new LinkedHashMap<String, String>();
    private final Map<String, String> headers = new LinkedHashMap<String, String>();
    private @Nullable RequestBody body;
    private RequestOptions options = RequestOptions.none();

    private Builder(HttpMethod method, String path) {
      this.method = method;
      this.path = path;
    }

    /** Adds a query parameter. */
    public Builder query(String name, String value) {
      query.put(requireNonBlank(name, "name"), Objects.requireNonNull(value, "value"));
      return this;
    }

    /** Adds a custom request header. */
    public Builder header(String name, String value) {
      headers.put(requireNonBlank(name, "name"), Objects.requireNonNull(value, "value"));
      return this;
    }

    /** Sets the request body. */
    public Builder body(RequestBody value) {
      this.body = Objects.requireNonNull(value, "value");
      return this;
    }

    /** Sets per-request options. */
    public Builder options(RequestOptions value) {
      this.options = Objects.requireNonNull(value, "value");
      return this;
    }

    /** Builds an immutable HTTP request. */
    public HttpRequest build() {
      return new HttpRequest(this);
    }
  }

  private static String requireNonBlank(String value, String name) {
    String checked = Objects.requireNonNull(value, name).trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return checked;
  }
}
