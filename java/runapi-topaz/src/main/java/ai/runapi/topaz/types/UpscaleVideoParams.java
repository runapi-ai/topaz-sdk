package ai.runapi.topaz.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for upscale video operations. */
public final class UpscaleVideoParams {
  private final String model;
  private final String sourceVideoUrl;
  private final Integer upscaleFactor;
  private final String callbackUrl;

  private UpscaleVideoParams(Builder builder) {
    this.model = builder.model;
    this.sourceVideoUrl = TopazParamUtils.requireNonBlank(builder.sourceVideoUrl, "sourceVideoUrl");
    this.upscaleFactor = builder.upscaleFactor;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new UpscaleVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "topaz/upscale-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", TopazParamUtils.wireValue(model));
    raw.put("source_video_url", TopazParamUtils.wireValue(sourceVideoUrl));
    raw.put("upscale_factor", TopazParamUtils.wireValue(upscaleFactor));
    raw.put("callback_url", TopazParamUtils.wireValue(callbackUrl));
    return TopazParamUtils.compact(raw);
  }



  /** Builder for {@link UpscaleVideoParams}. */
  public static final class Builder {
    private String model;
    private String sourceVideoUrl;
    private Integer upscaleFactor;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(UpscaleVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = TopazParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the source video URL. */
    public Builder sourceVideoUrl(String value) {
      this.sourceVideoUrl = TopazParamUtils.requireNonBlank(value, "sourceVideoUrl");
      return this;
    }

    /** Sets the upscale factor. */
    public Builder upscaleFactor(int value) {
      this.upscaleFactor = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = TopazParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable upscale video parameters. */
    public UpscaleVideoParams build() {
      return new UpscaleVideoParams(this);
    }
  }
}
