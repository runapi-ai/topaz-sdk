package ai.runapi.core.uploads;

import com.fasterxml.jackson.annotation.JsonProperty;

/** One uploaded Part belonging to an Upload. */
public final class UploadPart {
  private String id;
  private String object;
  @JsonProperty("created_at") private long createdAt;
  @JsonProperty("upload_id") private String uploadId;

  public String getId() { return id; }
  public String getObject() { return object; }
  public long getCreatedAt() { return createdAt; }
  public String getUploadId() { return uploadId; }
}
