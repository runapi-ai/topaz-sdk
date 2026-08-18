package ai.runapi.core.uploads;

import ai.runapi.core.files.FileObject;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Multipart Upload lifecycle resource. */
public final class UploadObject {
  private String id;
  private String object;
  private long bytes;
  @JsonProperty("created_at") private long createdAt;
  private String filename;
  private String purpose;
  private String status;
  @JsonProperty("expires_at") private long expiresAt;
  private FileObject file;

  public String getId() { return id; }
  public String getObject() { return object; }
  public long getBytes() { return bytes; }
  public long getCreatedAt() { return createdAt; }
  public String getFilename() { return filename; }
  public String getPurpose() { return purpose; }
  public String getStatus() { return status; }
  public long getExpiresAt() { return expiresAt; }
  public FileObject getFile() { return file; }
}
