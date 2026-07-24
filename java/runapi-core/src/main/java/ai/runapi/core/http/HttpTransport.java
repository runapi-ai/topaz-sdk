package ai.runapi.core.http;

import java.util.Map;

/** Sends HTTP requests for SDK clients. */
public interface HttpTransport extends AutoCloseable {
  /** Sends a request and returns the response body and metadata. */
  HttpResponse send(HttpRequest request);

  /**
   * PUT bytes straight to a pre-authorized absolute URL with the exact headers
   * issued for it — no base URL, no auth, no retries. Used for direct uploads to
   * a target that lives outside the API host. Transports that only speak to the
   * API may leave this unsupported.
   */
  default void upload(String url, Map<String, String> headers, byte[] body) {
    throw new UnsupportedOperationException("upload is not supported by this transport");
  }

  @Override
  void close();
}
