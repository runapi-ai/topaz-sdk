package ai.runapi.core;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Per-request HTTP and polling options that override client-level defaults. */
public final class RequestOptions {
  private static final RequestOptions NONE = new Builder().build();

  private final Map<String, String> headers;
  private final @Nullable Duration timeout;
  private final @Nullable Integer maxRetries;
  private final @Nullable Duration pollingInterval;
  private final @Nullable Duration pollingMaxWait;

  private RequestOptions(Builder builder) {
    this.headers = Collections.unmodifiableMap(new LinkedHashMap<String, String>(builder.headers));
    this.timeout = builder.timeout;
    this.maxRetries = builder.maxRetries;
    this.pollingInterval = builder.pollingInterval;
    this.pollingMaxWait = builder.pollingMaxWait;
  }

  /** Returns an empty immutable request-options instance. */
  public static RequestOptions none() {
    return NONE;
  }

  /** Creates a new request-options builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Additional per-request headers. */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /** HTTP request timeout override, if set. */
  public @Nullable Duration getTimeout() {
    return timeout;
  }

  /** Maximum retry attempts override, if set. */
  public @Nullable Integer getMaxRetries() {
    return maxRetries;
  }

  /** Polling interval override, if set. */
  public @Nullable Duration getPollingInterval() {
    return pollingInterval;
  }

  /** Polling maximum wait override, if set. */
  public @Nullable Duration getPollingMaxWait() {
    return pollingMaxWait;
  }

  /** Builder for {@link RequestOptions}. */
  public static final class Builder {
    private final Map<String, String> headers = new LinkedHashMap<String, String>();
    private @Nullable Duration timeout;
    private @Nullable Integer maxRetries;
    private @Nullable Duration pollingInterval;
    private @Nullable Duration pollingMaxWait;

    private Builder() {}

    /** Adds a custom header. SDK-managed headers override custom values when sending requests. */
    public Builder header(String name, String value) {
      String normalizedName = requireNonBlank(name, "name");
      String normalizedValue = Objects.requireNonNull(value, "value");
      removeHeaderIgnoreCase(headers, normalizedName);
      headers.put(normalizedName, normalizedValue);
      return this;
    }

    /** Sets additional custom headers. */
    public Builder headers(Map<String, String> values) {
      Objects.requireNonNull(values, "values");
      for (Map.Entry<String, String> entry : values.entrySet()) {
        header(entry.getKey(), entry.getValue());
      }
      return this;
    }

    /** Sets the HTTP request timeout override. */
    public Builder timeout(Duration value) {
      this.timeout = requirePositive(value, "timeout");
      return this;
    }

    /** Sets the maximum retry attempts. Use {@code 0} to disable retries. */
    public Builder maxRetries(int value) {
      if (value < 0) {
        throw new IllegalArgumentException("maxRetries must be >= 0");
      }
      this.maxRetries = value;
      return this;
    }

    /** Sets the polling interval override. */
    public Builder pollingInterval(Duration value) {
      this.pollingInterval = requirePositive(value, "pollingInterval");
      return this;
    }

    /** Sets the polling maximum wait override. */
    public Builder pollingMaxWait(Duration value) {
      this.pollingMaxWait = requirePositive(value, "pollingMaxWait");
      return this;
    }

    /** Builds immutable request options. */
    public RequestOptions build() {
      return new RequestOptions(this);
    }
  }

  static void removeHeaderIgnoreCase(Map<String, String> headers, String name) {
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

  static String requireNonBlank(String value, String name) {
    String checked = Objects.requireNonNull(value, name).trim();
    if (checked.isEmpty()) {
      throw new IllegalArgumentException(name + " must not be blank");
    }
    return checked;
  }

  static Duration requirePositive(Duration value, String name) {
    Duration checked = Objects.requireNonNull(value, name);
    if (checked.isZero() || checked.isNegative()) {
      throw new IllegalArgumentException(name + " must be positive");
    }
    return checked;
  }
}
