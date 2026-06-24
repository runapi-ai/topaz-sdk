package ai.runapi.topaz.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for upscale image operations. */
public final class UpscaleImageModel extends TopazValue {
  /** topaz-upscale-image model slug. */
  public static final UpscaleImageModel TOPAZ_UPSCALE_IMAGE = new UpscaleImageModel("topaz-upscale-image");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public UpscaleImageModel(String value) {
    super(value);
  }
}
