package ai.runapi.topaz.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for upscale video operations. */
public final class UpscaleVideoModel extends TopazValue {
  /** topaz-upscale-video model slug. */
  public static final UpscaleVideoModel TOPAZ_UPSCALE_VIDEO = new UpscaleVideoModel("topaz-upscale-video");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public UpscaleVideoModel(String value) {
    super(value);
  }
}
