package ai.runapi.core.files;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.RunApiException;
import ai.runapi.core.http.ApacheHttpTransport;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** File upload client. */
public final class FilesClient implements AutoCloseable {
  private static final String ENDPOINT = "/api/v1/files";
  private static final String PREPARE_ENDPOINT = ENDPOINT + "/prepare";
  private static final String CONFIRM_ENDPOINT = ENDPOINT + "/confirm";

  private final HttpTransport transport;
  private final ApiRequestExecutor executor;
  private final boolean ownsTransport;

  public FilesClient(HttpTransport transport, ClientOptions options, boolean ownsTransport) {
    this.transport = Objects.requireNonNull(transport, "transport");
    this.executor = new ApiRequestExecutor(transport, options);
    this.ownsTransport = ownsTransport;
  }

  /** Creates a new files client builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Creates a temporary file. */
  public FileUploadResponse create(FileCreateParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a temporary file with per-request options. */
  public FileUploadResponse create(FileCreateParams params, RequestOptions options) {
    Objects.requireNonNull(params, "params");
    Objects.requireNonNull(options, "options");
    Path path = params.getPath();
    if (path != null) {
      return uploadDirect(path, params.multipartFileName(), options);
    }
    HttpRequest request =
        HttpRequest.builder(HttpMethod.POST, ENDPOINT)
            .options(options)
            .body(new JsonRequestBody(params.toJsonBody()))
            .build();
    return executor.send(request, FileUploadResponse.class);
  }

  // Local files upload straight to storage: ask for a pre-authorized target, PUT
  // the bytes there (never through the API), then confirm. The caller still makes
  // a single create call.
  private FileUploadResponse uploadDirect(Path path, String fileName, RequestOptions options) {
    byte[] data;
    try {
      data = Files.readAllBytes(path);
    } catch (IOException e) {
      throw new RunApiException("Failed to read file: " + path, "validation_error", 0, null, null, e);
    }

    Map<String, Object> prepareBody = new LinkedHashMap<String, Object>();
    prepareBody.put("filename", fileName);
    prepareBody.put("byte_size", data.length);
    prepareBody.put("checksum", md5Base64(data));
    HttpRequest prepareRequest =
        HttpRequest.builder(HttpMethod.POST, PREPARE_ENDPOINT)
            .options(options)
            .body(new JsonRequestBody(prepareBody))
            .build();
    PrepareResponse prepared = executor.send(prepareRequest, PrepareResponse.class);

    transport.upload(prepared.getUploadUrl(), prepared.getHeaders(), data);

    HttpRequest confirmRequest =
        HttpRequest.builder(HttpMethod.POST, CONFIRM_ENDPOINT)
            .options(options)
            .body(new JsonRequestBody(Collections.<String, Object>singletonMap("signed_id", prepared.getSignedId())))
            .build();
    return executor.send(confirmRequest, FileUploadResponse.class);
  }

  private static String md5Base64(byte[] data) {
    try {
      byte[] digest = MessageDigest.getInstance("MD5").digest(data);
      return Base64.getEncoder().encodeToString(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("MD5 is not available", e);
    }
  }

  /** Closes the SDK-created transport, if this client owns it. */
  @Override
  public void close() {
    if (ownsTransport) {
      transport.close();
    }
  }

  /** Builder for {@link FilesClient}. */
  public static final class Builder {
    private final ClientOptions.Builder options = ClientOptions.builder();

    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    public Builder apiKey(String value) {
      options.apiKey(value);
      return this;
    }

    /** Sets the base URL. */
    public Builder baseUrl(String value) {
      options.baseUrl(value);
      return this;
    }

    /** Sets the base URL from a URI. */
    public Builder baseUrl(URI value) {
      options.baseUrl(value);
      return this;
    }

    /** Sets the default HTTP request timeout. */
    public Builder timeout(Duration value) {
      options.timeout(value);
      return this;
    }

    /** Sets the default maximum retry attempts. */
    public Builder maxRetries(int value) {
      options.maxRetries(value);
      return this;
    }

    /** Sets the retry base delay. */
    public Builder retryBaseDelay(Duration value) {
      options.retryBaseDelay(value);
      return this;
    }

    /** Sets the retry maximum delay. */
    public Builder retryMaxDelay(Duration value) {
      options.retryMaxDelay(value);
      return this;
    }

    /** Adds a custom client-level header. */
    public Builder header(String name, String value) {
      options.header(name, value);
      return this;
    }

    /** Adds custom client-level headers. */
    public Builder headers(Map<String, String> values) {
      options.headers(values);
      return this;
    }

    /** Sets a custom transport. User-provided transports are not closed by SDK clients. */
    public Builder transport(HttpTransport value) {
      options.transport(value);
      return this;
    }

    /** Builds a files client. */
    public FilesClient build() {
      ClientOptions resolved = options.build();
      HttpTransport configured = resolved.getTransport();
      if (configured != null) {
        return new FilesClient(configured, resolved, false);
      }
      return new FilesClient(new ApacheHttpTransport(resolved), resolved, true);
    }
  }
}
