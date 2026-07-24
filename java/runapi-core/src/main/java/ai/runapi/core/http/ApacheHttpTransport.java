package ai.runapi.core.http;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.Constants;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ErrorMapper;
import ai.runapi.core.errors.NetworkException;
import ai.runapi.core.errors.TimeoutException;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.util.Timeout;
import org.jspecify.annotations.Nullable;

/** Apache HttpClient 5 classic transport. */
public final class ApacheHttpTransport implements HttpTransport {
  private final ClientOptions options;
  private final CloseableHttpClient client;

  public ApacheHttpTransport(ClientOptions options) {
    this.options = Objects.requireNonNull(options, "options");
    this.client =
        HttpClients.custom()
            .setConnectionManager(
                PoolingHttpClientConnectionManagerBuilder.create()
                    .setDefaultConnectionConfig(connectionConfig(options.getTimeout()))
                    .build())
            .build();
  }

  /** Sends an SDK HTTP request using Apache HttpClient 5 classic. */
  @Override
  public HttpResponse send(HttpRequest request) {
    Objects.requireNonNull(request, "request");
    URI uri = buildUri(request);
    HttpUriRequestBase apacheRequest = new HttpUriRequestBase(request.getMethod().name(), uri);
    apacheRequest.setConfig(requestConfig(timeoutFor(request)));

    Map<String, String> headers = mergedHeaders(request);
    for (Map.Entry<String, String> header : headers.entrySet()) {
      apacheRequest.setHeader(header.getKey(), header.getValue());
    }

    RequestBody body = request.getBody();
    if (body != null && !(body instanceof EmptyRequestBody)) {
      apacheRequest.setEntity(new RequestBodyEntity(body));
    }

    try {
      return client.execute(
          apacheRequest,
          response -> {
            String responseBody = readEntity(response);
            if (response.getCode() < 200 || response.getCode() >= 300) {
              throw ErrorMapper.fromResponse(
                  response.getCode(), name -> firstHeader(response, name), responseBody, null);
            }
            return new HttpResponse(response.getCode(), responseBody, headersFrom(response));
          });
    } catch (java.net.SocketTimeoutException e) {
      throw new TimeoutException("Request timed out", e);
    } catch (IOException e) {
      throw new NetworkException("Network error", e);
    }
  }

  /** PUTs bytes to a pre-authorized absolute URL with the given headers (no auth). */
  @Override
  public void upload(String url, Map<String, String> headers, byte[] body) {
    HttpUriRequestBase request = new HttpUriRequestBase("PUT", URI.create(url));
    request.setConfig(requestConfig(options.getTimeout()));
    for (Map.Entry<String, String> header : headers.entrySet()) {
      request.setHeader(header.getKey(), header.getValue());
    }
    request.setEntity(new ByteArrayEntity(body, null));

    try {
      client.execute(
          request,
          response -> {
            if (response.getCode() < 200 || response.getCode() >= 300) {
              throw ErrorMapper.fromResponse(
                  response.getCode(), name -> firstHeader(response, name), readEntity(response), null);
            }
            return null;
          });
    } catch (java.net.SocketTimeoutException e) {
      throw new TimeoutException("Direct upload timed out", e);
    } catch (IOException e) {
      throw new NetworkException("Direct upload network error", e);
    }
  }

  /** Closes the underlying Apache HttpClient. */
  @Override
  public void close() {
    try {
      client.close();
    } catch (IOException e) {
      throw new NetworkException("failed to close HTTP transport", e);
    }
  }

  private URI buildUri(HttpRequest request) {
    String base = options.getBaseUrl().toString().replaceAll("/+$", "");
    String path = request.getPath().startsWith("/") ? request.getPath() : "/" + request.getPath();
    StringBuilder builder = new StringBuilder(base).append(path);
    if (!request.getQuery().isEmpty()) {
      builder.append("?");
      boolean first = true;
      for (Map.Entry<String, String> entry : request.getQuery().entrySet()) {
        if (!first) {
          builder.append("&");
        }
        first = false;
        builder
            .append(encode(entry.getKey()))
            .append("=")
            .append(encode(entry.getValue()));
      }
    }
    return URI.create(builder.toString());
  }

  private static String encode(String value) {
    try {
      return URLEncoder.encode(value, "UTF-8");
    } catch (java.io.UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

  private Duration timeoutFor(HttpRequest request) {
    Duration override = request.getOptions().getTimeout();
    return override != null ? override : options.getTimeout();
  }

  static RequestConfig requestConfig(Duration timeout) {
    Timeout apacheTimeout = Timeout.ofMilliseconds(Objects.requireNonNull(timeout, "timeout").toMillis());
    RequestConfig.Builder builder = RequestConfig.custom()
        .setResponseTimeout(apacheTimeout)
        .setConnectionRequestTimeout(apacheTimeout);
    setRequestScopedConnectTimeout(builder, apacheTimeout);
    return builder.build();
  }

  @SuppressWarnings("deprecation")
  private static void setRequestScopedConnectTimeout(RequestConfig.Builder builder, Timeout timeout) {
    // HttpClient 5.3 moved default connect timeout to ConnectionConfig, but
    // RequestConfig still owns per-request connect-timeout overrides.
    builder.setConnectTimeout(timeout);
  }

  static ConnectionConfig connectionConfig(Duration timeout) {
    Timeout apacheTimeout = Timeout.ofMilliseconds(Objects.requireNonNull(timeout, "timeout").toMillis());
    return ConnectionConfig.custom().setConnectTimeout(apacheTimeout).build();
  }

  private Map<String, String> mergedHeaders(HttpRequest request) {
    Map<String, String> headers = new LinkedHashMap<String, String>();
    mergeCustom(headers, options.getHeaders());
    mergeCustom(headers, request.getHeaders());
    mergeCustom(headers, request.getOptions().getHeaders());
    RequestBody body = request.getBody();
    setManaged(headers, "Authorization", "Bearer " + options.getApiKey());
    setManaged(headers, "Accept", "application/json");
    setManaged(headers, "User-Agent", Constants.SDK_USER_AGENT);
    if (body != null && body.contentType() != null) {
      setManaged(headers, "Content-Type", body.contentType());
    }
    return headers;
  }

  private static void mergeCustom(Map<String, String> target, Map<String, String> source) {
    for (Map.Entry<String, String> entry : source.entrySet()) {
      removeHeaderIgnoreCase(target, entry.getKey());
      target.put(entry.getKey(), entry.getValue());
    }
  }

  private static void setManaged(Map<String, String> target, String name, String value) {
    removeHeaderIgnoreCase(target, name);
    target.put(name, value);
  }

  private static void removeHeaderIgnoreCase(Map<String, String> headers, String name) {
    String target = name.toLowerCase(Locale.ROOT);
    String matched = null;
    for (String key : headers.keySet()) {
      if (key.toLowerCase(Locale.ROOT).equals(target)) {
        matched = key;
        break;
      }
    }
    if (matched != null) {
      headers.remove(matched);
    }
  }

  private static String readEntity(ClassicHttpResponse response) throws IOException {
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      return "";
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    entity.writeTo(out);
    return new String(out.toByteArray(), charsetFor(entity));
  }

  private static Charset charsetFor(HttpEntity entity) {
    // Honor the charset declared in the response Content-Type; fall back to UTF-8
    // when it is absent or unparseable so non-UTF-8 bodies are decoded correctly.
    try {
      ContentType contentType = ContentType.parse(entity.getContentType());
      if (contentType != null && contentType.getCharset() != null) {
        return contentType.getCharset();
      }
    } catch (RuntimeException ignored) {
      // Unsupported/malformed charset in Content-Type; fall back to UTF-8.
    }
    return StandardCharsets.UTF_8;
  }

  private static @Nullable String firstHeader(ClassicHttpResponse response, String name) {
    Header header = response.getFirstHeader(name);
    return header == null ? null : header.getValue();
  }

  private static Map<String, List<String>> headersFrom(ClassicHttpResponse response) {
    Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
    for (Header header : response.getHeaders()) {
      List<String> values = headers.get(header.getName());
      if (values == null) {
        values = new ArrayList<String>();
        headers.put(header.getName(), values);
      }
      values.add(header.getValue());
    }
    return headers;
  }

  private static final class RequestBodyEntity extends AbstractHttpEntity {
    private final RequestBody body;

    private RequestBodyEntity(RequestBody body) {
      super(contentType(body), null, false);
      this.body = body;
    }

    /** Returns whether the wrapped SDK request body can be sent more than once. */
    @Override
    public boolean isRepeatable() {
      return body.isRepeatable();
    }

    /** Returns the wrapped body content length, or {@code -1} when unknown. */
    @Override
    public long getContentLength() {
      Long length = body.contentLength();
      return length == null ? -1L : length;
    }

    /** Returns whether this entity streams its content. */
    @Override
    public boolean isStreaming() {
      return !body.isRepeatable();
    }

    /** Returns an input stream for Apache HttpClient when requested. */
    @Override
    public InputStream getContent() throws IOException {
      if (body.isRepeatable()) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        body.writeTo(out);
        return new java.io.ByteArrayInputStream(out.toByteArray());
      }
      final PipedInputStream input = new PipedInputStream();
      final PipedOutputStream output = new PipedOutputStream(input);
      final AtomicReference<IOException> writeError = new AtomicReference<>();
      Thread writer =
          new Thread(
              () -> {
                try {
                  body.writeTo(output);
                } catch (IOException e) {
                  // Capture the real failure so the reader can rethrow it. Closing the
                  // pipe alone would surface only a generic "Pipe closed" and could even
                  // look like a cleanly-finished (truncated) body.
                  writeError.set(e);
                } finally {
                  try {
                    output.close();
                  } catch (IOException ignored) {
                    // Ignore secondary close failure.
                  }
                }
              },
              "runapi-request-body-writer");
      writer.setDaemon(true);
      writer.start();
      // Wrap the read end so that when the stream ends, any writer-side failure is
      // rethrown with its original cause instead of being silently swallowed.
      return new FilterInputStream(input) {
        private void rethrowWriteError() throws IOException {
          IOException error = writeError.get();
          if (error != null) {
            throw new IOException("Failed to stream request body: " + error.getMessage(), error);
          }
        }

        @Override
        public int read() throws IOException {
          int value = super.read();
          if (value == -1) {
            rethrowWriteError();
          }
          return value;
        }

        @Override
        public int read(byte[] buffer, int off, int len) throws IOException {
          int read = super.read(buffer, off, len);
          if (read == -1) {
            rethrowWriteError();
          }
          return read;
        }
      };
    }

    /** Writes the wrapped SDK request body to Apache HttpClient's output stream. */
    @Override
    public void writeTo(OutputStream outStream) throws IOException {
      body.writeTo(outStream);
    }

    /** Closes this entity. The wrapped SDK body owns any stream lifecycle. */
    @Override
    public void close() throws IOException {}

    private static @Nullable ContentType contentType(RequestBody body) {
      return body.contentType() == null ? null : ContentType.parse(body.contentType());
    }
  }
}
