package ai.runapi.topaz.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for upscale image operations. */
public final class UpscaleImageParams {
  private final String model;
  private final String sourceImageUrl;
  private final Integer upscaleFactor;
  private final String callbackUrl;

  private UpscaleImageParams(Builder builder) {
    this.model = builder.model;
    this.sourceImageUrl = TopazParamUtils.requireNonBlank(builder.sourceImageUrl, "sourceImageUrl");
    this.upscaleFactor = java.util.Objects.requireNonNull(builder.upscaleFactor, "upscaleFactor");
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new UpscaleImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "topaz/upscale-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", TopazParamUtils.wireValue(model));
    raw.put("source_image_url", TopazParamUtils.wireValue(sourceImageUrl));
    raw.put("upscale_factor", TopazParamUtils.wireValue(upscaleFactor));
    raw.put("callback_url", TopazParamUtils.wireValue(callbackUrl));
    return TopazParamUtils.compact(raw);
  }



  /** Builder for {@link UpscaleImageParams}. */
  public static final class Builder {
    private String model;
    private String sourceImageUrl;
    private Integer upscaleFactor;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(UpscaleImageModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = TopazParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the source image URL. */
    public Builder sourceImageUrl(String value) {
      this.sourceImageUrl = TopazParamUtils.requireNonBlank(value, "sourceImageUrl");
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

    /** Builds immutable upscale image parameters. */
    public UpscaleImageParams build() {
      return new UpscaleImageParams(this);
    }
  }
}
