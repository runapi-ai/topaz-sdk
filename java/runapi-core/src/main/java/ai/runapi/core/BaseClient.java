package ai.runapi.core;

import ai.runapi.core.account.AccountClient;
import ai.runapi.core.files.FilesClient;
import ai.runapi.core.http.ApacheHttpTransport;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/** Base class for RunAPI Java clients. */
public class BaseClient implements AutoCloseable {
  private final HttpTransport transport;
  private final ClientOptions options;
  private final boolean ownsTransport;
  private final FilesClient files;
  private final AccountClient account;

  /** Creates a base client from resolved options. */
  protected BaseClient(ClientOptions options) {
    Objects.requireNonNull(options, "options");
    this.options = options;
    HttpTransport configured = options.getTransport();
    if (configured != null) {
      this.transport = configured;
      this.ownsTransport = false;
    } else {
      this.transport = new ApacheHttpTransport(options);
      this.ownsTransport = true;
    }
    this.files = new FilesClient(transport, options, false);
    this.account = new AccountClient(transport, options, false);
  }

  /** Creates a base client with default builder options. */
  protected BaseClient() {
    this(ClientOptions.builder().build());
  }

  /** Creates a new base client builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** File upload operations. */
  public final FilesClient files() {
    return files;
  }

  /** Account info and balance operations. */
  public final AccountClient account() {
    return account;
  }

  /** Shared HTTP transport for subclasses. */
  protected final HttpTransport transport() {
    return transport;
  }

  /** Resolved options for subclasses. */
  protected final ClientOptions options() {
    return options;
  }

  /** Closes the SDK-created transport, if this client owns it. */
  @Override
  public void close() {
    if (ownsTransport) {
      transport.close();
    }
  }

  /** Builder for {@link BaseClient}. */
  public static class Builder<T extends Builder<T>> {
    protected final ClientOptions.Builder options = ClientOptions.builder();

    protected Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    public T apiKey(String value) {
      options.apiKey(value);
      return self();
    }

    /** Sets the base URL. */
    public T baseUrl(String value) {
      options.baseUrl(value);
      return self();
    }

    /** Sets the base URL from a URI. */
    public T baseUrl(URI value) {
      options.baseUrl(value);
      return self();
    }

    /** Sets the default HTTP request timeout. */
    public T timeout(Duration value) {
      options.timeout(value);
      return self();
    }

    /** Sets the default maximum retry attempts. */
    public T maxRetries(int value) {
      options.maxRetries(value);
      return self();
    }

    /** Sets the retry base delay. */
    public T retryBaseDelay(Duration value) {
      options.retryBaseDelay(value);
      return self();
    }

    /** Sets the retry maximum delay. */
    public T retryMaxDelay(Duration value) {
      options.retryMaxDelay(value);
      return self();
    }

    /** Sets the default polling interval for synchronous run methods. */
    public T pollingInterval(Duration value) {
      options.pollingInterval(value);
      return self();
    }

    /** Sets the default polling maximum wait for synchronous run methods. */
    public T pollingMaxWait(Duration value) {
      options.pollingMaxWait(value);
      return self();
    }

    /** Adds a custom client-level header. */
    public T header(String name, String value) {
      options.header(name, value);
      return self();
    }

    /** Adds custom client-level headers. */
    public T headers(Map<String, String> values) {
      options.headers(values);
      return self();
    }

    /** Sets a custom transport. User-provided transports are not closed by SDK clients. */
    public T transport(HttpTransport value) {
      options.transport(value);
      return self();
    }

    /** Builds a base client. */
    public BaseClient build() {
      return new BaseClient(options.build());
    }

    @SuppressWarnings("unchecked")
    protected T self() {
      return (T) this;
    }
  }
}
