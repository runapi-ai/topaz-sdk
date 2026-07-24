package ai.runapi.core.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** Internal response from the direct-upload prepare step: where and how to PUT. */
final class PrepareResponse {
  @JsonProperty("signed_id")
  private String signedId;

  @JsonProperty("upload_url")
  private String uploadUrl;

  @JsonProperty("headers")
  private @Nullable Map<String, String> headers;

  String getSignedId() {
    return signedId;
  }

  String getUploadUrl() {
    return uploadUrl;
  }

  Map<String, String> getHeaders() {
    return headers == null ? Collections.<String, String>emptyMap() : headers;
  }
}
