package ai.runapi.core;

import ai.runapi.core.errors.AuthenticationException;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Resolved client-level configuration shared by SDK clients. */
public final class ClientOptions {
  private static final String API_KEY_ENV = "RUNAPI_API_KEY";
  private static final String BASE_URL_ENV = "RUNAPI_BASE_URL";

  private final String apiKey;
  private final URI baseUrl;
  private final Duration timeout;
  private final int maxRetries;
  private final Duration retryBaseDelay;
  private final Duration retryMaxDelay;
  private final Duration pollingInterval;
  private final Duration pollingMaxWait;
  private final Map<String, String> headers;
  private final @Nullable HttpTransport transport;

  private ClientOptions(Builder builder) {
    this.apiKey = resolveApiKey(builder.apiKey);
    this.baseUrl = resolveBaseUrl(builder.baseUrl);
    this.timeout = builder.timeout != null ? builder.timeout : Constants.DEFAULT_REQUEST_TIMEOUT;
    this.maxRetries = builder.maxRetries != null ? builder.maxRetries : Constants.DEFAULT_MAX_RETRIES;
    this.retryBaseDelay =
        builder.retryBaseDelay != null ? builder.retryBaseDelay : Constants.DEFAULT_RETRY_BASE_DELAY;
    this.retryMaxDelay =
        builder.retryMaxDelay != null ? builder.retryMaxDelay : Constants.DEFAULT_RETRY_MAX_DELAY;
    this.pollingInterval =
        builder.pollingInterval != null ? builder.pollingInterval : Constants.DEFAULT_POLLING_INTERVAL;
    this.pollingMaxWait =
        builder.pollingMaxWait != null ? builder.pollingMaxWait : Constants.DEFAULT_POLLING_MAX_WAIT;
    this.headers = Collections.unmodifiableMap(new LinkedHashMap<String, String>(builder.headers));
    this.transport = builder.transport;
  }

  /** Creates a new client-options builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Resolved API key. */
  public String getApiKey() {
    return apiKey;
  }

  /** Resolved base URL. */
  public URI getBaseUrl() {
    return baseUrl;
  }

  /** Default HTTP request timeout. */
  public Duration getTimeout() {
    return timeout;
  }

  /** Default maximum retry attempts. */
  public int getMaxRetries() {
    return maxRetries;
  }

  /** Retry base delay. */
  public Duration getRetryBaseDelay() {
    return retryBaseDelay;
  }

  /** Retry maximum delay. */
  public Duration getRetryMaxDelay() {
    return retryMaxDelay;
  }

  /** Default polling interval. */
  public Duration getPollingInterval() {
    return pollingInterval;
  }

  /** Default polling maximum wait. */
  public Duration getPollingMaxWait() {
    return pollingMaxWait;
  }

  /** Custom client-level headers. */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /** User-provided transport, if any. */
  public @Nullable HttpTransport getTransport() {
    return transport;
  }

  /** Builder for {@link ClientOptions}. */
  public static final class Builder {
    private @Nullable String apiKey;
    private @Nullable URI baseUrl;
    private @Nullable Duration timeout;
    private @Nullable Integer maxRetries;
    private @Nullable Duration retryBaseDelay;
    private @Nullable Duration retryMaxDelay;
    private @Nullable Duration pollingInterval;
    private @Nullable Duration pollingMaxWait;
    private final Map<String, String> headers = new LinkedHashMap<String, String>();
    private @Nullable HttpTransport transport;

    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    public Builder apiKey(String value) {
      this.apiKey = RequestOptions.requireNonBlank(value, "apiKey");
      return this;
    }

    /** Sets the base URL from a string. */
    public Builder baseUrl(String value) {
      return baseUrl(URI.create(RequestOptions.requireNonBlank(value, "baseUrl")));
    }

    /** Sets the base URL from a URI. */
    public Builder baseUrl(URI value) {
      this.baseUrl = validateBaseUrl(value);
      return this;
    }

    /** Sets the default HTTP request timeout. */
    public Builder timeout(Duration value) {
      this.timeout = RequestOptions.requirePositive(value, "timeout");
      return this;
    }

    /** Sets the default maximum retry attempts. */
    public Builder maxRetries(int value) {
      if (value < 0) {
        throw new IllegalArgumentException("maxRetries must be >= 0");
      }
      this.maxRetries = value;
      return this;
    }

    /** Sets the retry base delay. */
    public Builder retryBaseDelay(Duration value) {
      this.retryBaseDelay = RequestOptions.requirePositive(value, "retryBaseDelay");
      return this;
    }

    /** Sets the retry maximum delay. */
    public Builder retryMaxDelay(Duration value) {
      this.retryMaxDelay = RequestOptions.requirePositive(value, "retryMaxDelay");
      return this;
    }

    /** Sets the default polling interval. */
    public Builder pollingInterval(Duration value) {
      this.pollingInterval = RequestOptions.requirePositive(value, "pollingInterval");
      return this;
    }

    /** Sets the default polling maximum wait. */
    public Builder pollingMaxWait(Duration value) {
      this.pollingMaxWait = RequestOptions.requirePositive(value, "pollingMaxWait");
      return this;
    }

    /** Adds a custom client-level header. */
    public Builder header(String name, String value) {
      String normalizedName = RequestOptions.requireNonBlank(name, "name");
      String normalizedValue = Objects.requireNonNull(value, "value");
      RequestOptions.removeHeaderIgnoreCase(headers, normalizedName);
      headers.put(normalizedName, normalizedValue);
      return this;
    }

    /** Adds custom client-level headers. */
    public Builder headers(Map<String, String> values) {
      Objects.requireNonNull(values, "values");
      for (Map.Entry<String, String> entry : values.entrySet()) {
        header(entry.getKey(), entry.getValue());
      }
      return this;
    }

    /** Sets a custom transport. User-provided transports are not closed by SDK clients. */
    public Builder transport(HttpTransport value) {
      this.transport = Objects.requireNonNull(value, "value");
      return this;
    }

    /** Builds resolved client options. */
    public ClientOptions build() {
      return new ClientOptions(this);
    }
  }

  private static String resolveApiKey(@Nullable String explicit) {
    if (explicit != null && !explicit.trim().isEmpty()) {
      return explicit.trim();
    }
    String fromEnv = System.getenv(API_KEY_ENV);
    if (fromEnv != null && !fromEnv.trim().isEmpty()) {
      return fromEnv.trim();
    }
    throw new AuthenticationException(
        "API key is required. Pass apiKey or set the RUNAPI_API_KEY environment variable.");
  }

  private static URI resolveBaseUrl(@Nullable URI explicit) {
    if (explicit != null) {
      return validateBaseUrl(explicit);
    }
    String fromEnv = System.getenv(BASE_URL_ENV);
    if (fromEnv != null && !fromEnv.trim().isEmpty()) {
      return validateBaseUrl(URI.create(fromEnv.trim()));
    }
    return URI.create(Constants.DEFAULT_BASE_URL);
  }

  private static URI validateBaseUrl(URI value) {
    URI checked = Objects.requireNonNull(value, "baseUrl");
    String scheme = checked.getScheme();
    if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
      throw new IllegalArgumentException("baseUrl scheme must be http or https");
    }
    if (checked.getHost() == null || checked.getHost().trim().isEmpty()) {
      throw new IllegalArgumentException("baseUrl host is required");
    }
    return checked;
  }
}
