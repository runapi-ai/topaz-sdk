package ai.runapi.core.files;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Metadata returned after creating a temporary file. */
public final class FileUploadResponse {
  @JsonProperty("file_name")
  private String fileName;

  @JsonProperty("url")
  private String url;

  @JsonProperty("size_bytes")
  private long sizeBytes;

  @JsonProperty("mime_type")
  private String mimeType;

  @JsonProperty("created_at")
  private Instant createdAt;

  @JsonProperty("expires_at")
  private Instant expiresAt;

  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** File name stored by RunAPI. */
  public String getFileName() {
    return fileName;
  }

  /** File URL for use in generation requests. */
  public String getUrl() {
    return url;
  }

  /** Size in bytes. */
  public long getSizeBytes() {
    return sizeBytes;
  }

  /** Detected MIME type. */
  public String getMimeType() {
    return mimeType;
  }

  /** Creation time. */
  public Instant getCreatedAt() {
    return createdAt;
  }

  /** Expiration time. */
  public Instant getExpiresAt() {
    return expiresAt;
  }

  /** Unknown response fields preserved as JSON nodes. */
  public Map<String, JsonNode> extraFields() {
    return Collections.unmodifiableMap(extraFields);
  }

  @JsonAnySetter
  void putExtraField(String name, JsonNode value) {
    extraFields.put(name, value);
  }
}
