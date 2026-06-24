package ai.runapi.core.account;

import ai.runapi.core.ApiRequestExecutor;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.ApacheHttpTransport;
import ai.runapi.core.http.HttpMethod;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/** Account info and balance client. */
public final class AccountClient implements AutoCloseable {
  private static final String INFO_ENDPOINT = "/api/v1/me";
  private static final String BALANCE_ENDPOINT = "/api/v1/me/balance";

  private final HttpTransport transport;
  private final ApiRequestExecutor executor;
  private final boolean ownsTransport;

  public AccountClient(HttpTransport transport, ClientOptions options, boolean ownsTransport) {
    this.transport = Objects.requireNonNull(transport, "transport");
    this.executor = new ApiRequestExecutor(transport, options);
    this.ownsTransport = ownsTransport;
  }

  /** Creates a new account client builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns profile and account details for the authenticated API key. */
  public AccountInfoResponse info() {
    return info(RequestOptions.none());
  }

  /** Returns profile and account details with per-request options. */
  public AccountInfoResponse info(RequestOptions options) {
    return executor.send(
        HttpRequest.builder(HttpMethod.GET, INFO_ENDPOINT).options(Objects.requireNonNull(options, "options")).build(),
        AccountInfoResponse.class);
  }

  /** Returns the account balance and usage totals. */
  public AccountBalanceResponse balance() {
    return balance(RequestOptions.none());
  }

  /** Returns the account balance and usage totals with per-request options. */
  public AccountBalanceResponse balance(RequestOptions options) {
    return executor.send(
        HttpRequest.builder(HttpMethod.GET, BALANCE_ENDPOINT)
            .options(Objects.requireNonNull(options, "options"))
            .build(),
        AccountBalanceResponse.class);
  }

  /** Closes the SDK-created transport, if this client owns it. */
  @Override
  public void close() {
    if (ownsTransport) {
      transport.close();
    }
  }

  /** Builder for {@link AccountClient}. */
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

    /** Builds an account client. */
    public AccountClient build() {
      ClientOptions resolved = options.build();
      HttpTransport configured = resolved.getTransport();
      if (configured != null) {
        return new AccountClient(configured, resolved, false);
      }
      return new AccountClient(new ApacheHttpTransport(resolved), resolved, true);
    }
  }
}
