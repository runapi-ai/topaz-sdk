package ai.runapi.core.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Immutable HTTP response returned by SDK transports. */
public final class HttpResponse {
  private final int statusCode;
  private final String body;
  private final Map<String, List<String>> headers;

  public HttpResponse(int statusCode, String body, Map<String, List<String>> headers) {
    this.statusCode = statusCode;
    this.body = body;
    this.headers = Collections.unmodifiableMap(new LinkedHashMap<String, List<String>>(headers));
  }

  /** Returns the numeric HTTP status code. */
  public int getStatusCode() {
    return statusCode;
  }

  /** Returns the response body as a string. */
  public String getBody() {
    return body;
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
