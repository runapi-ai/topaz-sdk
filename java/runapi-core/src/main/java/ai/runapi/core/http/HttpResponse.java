package ai.runapi.core.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.jspecify.annotations.Nullable;

/** Immutable HTTP response returned by SDK transports. */
public final class HttpResponse {
  private final int statusCode;
  private final byte[] body;
  private final Charset bodyCharset;
  private volatile @Nullable String bodyText;
  private final Map<String, List<String>> headers;

  public HttpResponse(int statusCode, String body, Map<String, List<String>> headers) {
    this(statusCode, body.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8, body, headers, false);
  }

  public HttpResponse(int statusCode, byte[] body, Map<String, List<String>> headers) {
    this(statusCode, body, StandardCharsets.UTF_8, null, headers, true);
  }

  public HttpResponse(int statusCode, byte[] body, String bodyText, Map<String, List<String>> headers) {
    this(statusCode, body, StandardCharsets.UTF_8, bodyText, headers, true);
  }

  static HttpResponse fromOwnedBytes(
      int statusCode, byte[] body, Charset bodyCharset, Map<String, List<String>> headers) {
    return new HttpResponse(statusCode, body, bodyCharset, null, headers, false);
  }

  private HttpResponse(
      int statusCode,
      byte[] body,
      Charset bodyCharset,
      @Nullable String bodyText,
      Map<String, List<String>> headers,
      boolean copyBody) {
    this.statusCode = statusCode;
    this.body = copyBody ? body.clone() : body;
    this.bodyCharset = bodyCharset;
    this.bodyText = bodyText;
    this.headers = Collections.unmodifiableMap(new LinkedHashMap<String, List<String>>(headers));
  }

  /** Returns the numeric HTTP status code. */
  public int getStatusCode() {
    return statusCode;
  }

  /** Returns the response body as a string. */
  public String getBody() {
    String decoded = bodyText;
    if (decoded == null) {
      decoded = new String(body, bodyCharset);
      bodyText = decoded;
    }
    return decoded;
  }

  /** Returns an exact copy of the response body bytes. */
  public byte[] getBodyBytes() {
    return body.clone();
  }

  /** Returns immutable response headers. */
  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  /** Returns the first response header value matching the supplied name, case-insensitively. */
  public @Nullable String firstHeader(String name) {
    String target = name.toLowerCase(Locale.ROOT);
    for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
      if (entry.getKey().toLowerCase(Locale.ROOT).equals(target) && !entry.getValue().isEmpty()) {
        return entry.getValue().get(0);
      }
    }
    return null;
  }
}
