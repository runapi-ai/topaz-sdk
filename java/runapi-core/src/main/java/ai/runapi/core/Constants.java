package ai.runapi.core;

import java.time.Duration;

/** SDK defaults shared across RunAPI Java clients. */
public final class Constants {
  /** Production RunAPI endpoint. */
  public static final String DEFAULT_BASE_URL = "https://runapi.ai";

  /** SDK User-Agent header value. */
  public static final String SDK_USER_AGENT = "runapi-sdk-java/0.1.1";

  /** Default HTTP request timeout. */
  public static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofMinutes(15);

  /** Default polling timeout. */
  public static final Duration DEFAULT_POLLING_MAX_WAIT = Duration.ofMinutes(15);

  /** Default polling interval. */
  public static final Duration DEFAULT_POLLING_INTERVAL = Duration.ofSeconds(2);

  /** Default maximum retry attempts. */
  public static final int DEFAULT_MAX_RETRIES = 2;

  /** Default retry base delay. */
  public static final Duration DEFAULT_RETRY_BASE_DELAY = Duration.ofMillis(500);

  /** Default retry maximum delay. */
  public static final Duration DEFAULT_RETRY_MAX_DELAY = Duration.ofSeconds(5);

  private Constants() {}
}
