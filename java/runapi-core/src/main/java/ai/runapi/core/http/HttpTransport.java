package ai.runapi.core.http;

/** Sends HTTP requests for SDK clients. */
public interface HttpTransport extends AutoCloseable {
  /** Sends a request and returns the response body and metadata. */
  HttpResponse send(HttpRequest request);

  @Override
  void close();
}
