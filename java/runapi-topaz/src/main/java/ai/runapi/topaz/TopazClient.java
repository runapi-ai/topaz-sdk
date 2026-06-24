package ai.runapi.topaz;

import ai.runapi.core.BaseClient;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import ai.runapi.topaz.resources.UpscaleImageResource;
import ai.runapi.topaz.resources.UpscaleVideoResource;

/** Topaz model-family Java SDK client. */
public final class TopazClient extends BaseClient {
  private final UpscaleImageResource upscaleImage;
  private final UpscaleVideoResource upscaleVideo;

  private TopazClient(ClientOptions options) {
    super(options);
    this.upscaleImage = new UpscaleImageResource(transport(), options());
    this.upscaleVideo = new UpscaleVideoResource(transport(), options());
  }

  /** Creates a new TopazClient builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Upscale Image operations. */
  public UpscaleImageResource upscaleImage() {
    return upscaleImage;
  }

  /** Upscale Video operations. */
  public UpscaleVideoResource upscaleVideo() {
    return upscaleVideo;
  }

  /** Builder for {@link TopazClient}. */
  public static final class Builder extends BaseClient.Builder<Builder> {
    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    @Override
    public Builder apiKey(String value) {
      return super.apiKey(value);
    }

    /** Sets the RunAPI base URL. If omitted, the SDK reads {@code RUNAPI_BASE_URL}. */
    @Override
    public Builder baseUrl(String value) {
      return super.baseUrl(value);
    }

    /** Sets the RunAPI base URL from a URI. */
    @Override
    public Builder baseUrl(URI value) {
      return super.baseUrl(value);
    }

    /** Sets a custom HTTP transport. User-provided transports are not closed by SDK clients. */
    @Override
    public Builder transport(HttpTransport value) {
      return super.transport(value);
    }

    /** Builds an immutable TopazClient. */
    @Override
    public TopazClient build() {
      return new TopazClient(options.build());
    }
  }
}
