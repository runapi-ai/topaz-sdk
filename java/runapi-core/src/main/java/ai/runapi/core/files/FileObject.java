package ai.runapi.core.files;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Metadata for a File resource. */
public final class FileObject {
  private String id;
  private String object;
  private long bytes;
  @JsonProperty("created_at") private long createdAt;
  @JsonProperty("expires_at") private Long expiresAt;
  private String filename;
  private String purpose;

  public String getId() { return id; }
  public String getObject() { return object; }
  public long getBytes() { return bytes; }
  public long getCreatedAt() { return createdAt; }
  public Long getExpiresAt() { return expiresAt; }
  public String getFilename() { return filename; }
  public String getPurpose() { return purpose; }
}
